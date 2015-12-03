/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers;

import authentication.ActionAuthenticator;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Security.AuthenticatedAction;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class PostController extends Controller {

    private PostRepository postRepository;
    private FollowingRepository followingRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private ShareRepository shareRepository;
    private SearchController searchController;
    private long latestID = 0;
    private int topK = 10;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public PostController(PostRepository postRepository, FollowingRepository followingRepository,
                          UserRepository userRepository, CommentRepository commentRepository,
                          SearchController searchController, ShareRepository shareRepository) {
        this.postRepository = postRepository;
        this.followingRepository = followingRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.shareRepository = shareRepository;
        this.searchController = searchController;
        latestID = this.postRepository.latestID();
    }


    public Result getPost(long id) {

        List<Post> result = postRepository.findPost(id);
        for (Post p : result) {
            User user = userRepository.findByID(p.getAuthorID());
            p.setAuthorName(user.getUserName());
        }
        return ok(new Gson().toJson(result));
    }


    /**
     * Return the post of the id and its followers(for personal wall)
     *
     * @param id
     * @return
     */
    @Security.Authenticated(ActionAuthenticator.class)
    public Result getPersonalMainWall(long id) {
        //add both the post of id itself and its following users
        List<Following> followingList = followingRepository.findFollowedPeopleByID(id);
        List<Post> posts = new ArrayList<>(postRepository.findPost(id));
        List<PostAndComment> postAndComments = new ArrayList<PostAndComment>();
        for (Following f : followingList) posts.addAll(postRepository.findPost(f.getTarget()));
        Collections.sort(posts);
        for (Post p : posts) {
            String security = p.getSecurity();
            if (security != null && (p.getAuthorID() != id && p.getSecurity().equals("private"))) continue;
            User user = userRepository.findByID(p.getAuthorID());
            p.setAuthorName(user.getUserName());
            List<Comment> comments = postRepository.findComment(p.getId());
            postAndComments.add(new PostAndComment(p, comments));
        }
        return ok(new Gson().toJson(postAndComments));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result getHomeWall(long id) {
        //add both the post of id itself and its following users
        List<Post> posts = new ArrayList<>(postRepository.findPost(id));
        List<Share> shares = shareRepository.findBySharerId(id);
        for (Share share :  shares) {
            posts.add(share.getPost());
        }
        List<PostAndComment> postAndComments = new ArrayList<PostAndComment>();
        Collections.sort(posts);
        for (Post p : posts) {
            User user = userRepository.findByID(p.getAuthorID());
            p.setAuthorName(user.getUserName());
            List<Comment> comments = postRepository.findComment(p.getId());
            postAndComments.add(new PostAndComment(p, comments));
        }
        return ok(new Gson().toJson(postAndComments));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result getPopularPost(String viewerID) {
        List<Post> popular = postRepository.findPopularPost();
        Collections.sort(popular, new likesComparator());
        List<Post> result = new ArrayList<>();
        for(Post p : popular){
            if(result.size()==topK) break;
            String security = p.getSecurity();
            if (security != null && (p.getAuthorID()!=Long.parseLong(viewerID)&& p.getSecurity().equals("private"))) continue;
            result.add(p);
        }
        return ok(new Gson().toJson(result));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result publishPost() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not created, expecting Json data");
            return badRequest("Post not created, expecting Json data");
        }

        // Parse JSON file
        String author = json.path("authorId").asText();
        String content = json.path("content").asText();
        String security = json.path("security").asText();
        String location = json.path("location").asText();
        try {
            long authorId = Long.parseLong(author);
            long time = System.currentTimeMillis();
            String authorName = userRepository.findOne(authorId).getUserName();
            Post post = new Post(authorId, content, 0, time, authorName, location);
            post.setSecurity(security);
            postRepository.save(post);
            System.out.println("Post succesfully saved: " + post.getId());
            latestID++;
            searchController.appendPost(latestID, content);
            return created(new Gson().toJson(post.getId()));
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post not saved: " + content);
            return badRequest("Post not saved: " + content);
        }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result sharePost() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not identified, expecting Json data");
            return badRequest("Post not identified, expecting Json data");
        }

        String postId = json.path("postId").asText();
        long postIdLong = new Long(postId);
        Post post = postRepository.findOne(postIdLong);
        if (post == null) {
            System.out.println("post not found with id: " + postId);
            return notFound("post not found with id: " + postId);
        }

        long sharerId = Long.parseLong(json.path("sharerId").asText());
        List<Share> sharedPosts = shareRepository.findBySharerId(sharerId);
        for (Share share: sharedPosts) {
            if (share.getPost().getId() == postIdLong) {
                System.out.println("post " + postIdLong + " has already been shared by the user");
                return ok("post " + postIdLong + " has already been shared by the user");
            }
        }
        User sharer = userRepository.findOne(sharerId);
        Share newShare = new Share(post, sharer);
        shareRepository.save(newShare);
        System.out.println("Share successfully saved: " + newShare.getId());
        return created(new Gson().toJson(newShare.getId()));

    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result updatePost() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not identified, expecting Json data");
            return badRequest("Post not identified, expecting Json data");
        }

        // Parse JSON file
        String postId = json.path("postId").asText();
        long postIdLong = new Long(postId);
        Post post = postRepository.findOne(postIdLong);
        if (post == null) {
            System.out.println("post not found with id: " + postId);
            return notFound("post not found with id: " + postId);
        }

        if (json.path("content") != null && !json.path("content").asText().isEmpty()) {
            post.setContent(json.path("content").asText());
        }

        post.setTimeStamp(System.currentTimeMillis());
        if (json.path("numOfLikes") != null && !json.path("numOfLikes").asText().isEmpty()) {
            int likes = post.getLikes();
            post.setLikes(likes + 1);
        }

        postRepository.save(post);
        searchController.updatePost(postIdLong, post.getContent());
        return ok("post: " + postId + " is updated");
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result addComment() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Comment is missing");
            return badRequest("Comment is missing");
        }

        //Parse JSON file
        Long postId = Long.parseLong(json.path("postId").asText());
        Long commenterId = Long.parseLong(json.path("commenterId").asText());
        Post post = postRepository.findOne(postId);
        User commenter = userRepository.findOne(commenterId);
        String authorName = commenter.getFirstName();
        String content = json.path("content").asText();
        Comment comment = new Comment(post, commenter, content, authorName, System.currentTimeMillis());
        commentRepository.save(comment);
        System.out.println("Comment successfully saved: " + comment.getId());
        return created(new Gson().toJson(comment.getId()));

    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result deletePost() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not identified, expecting Json data");
            return badRequest("Post not identified, expecting Json data");
        }

        // Parse JSON file
        String postId = json.path("postId").asText();
        long postIdLong = new Long(postId);
        Post post = postRepository.findOne(postIdLong);
        if (post == null) {
            System.out.println("post not found with id: " + postId);
            return notFound("post not found with id: " + postId);
        }

        postRepository.delete(post);
        searchController.deletePost(postIdLong);
        return ok("post: " + postId + " is deleted");
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result changeSecurity(String postID, String security) {
        List<Post> posts = postRepository.findPostByPostID(Long.valueOf(postID));
        Post p = posts.get(0);
        p.setSecurity(security);
        postRepository.save(p);
        return ok("security: change to " + p.getSecurity());
    }

    private class likesComparator implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            if (o1.getLikes() > o2.getLikes()) return -1;
            else if (o1.getLikes() == o2.getLikes()) return 0;
            else return 1;
        }
    }

}
