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

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collections;
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

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public PostController(PostRepository postRepository, FollowingRepository followingRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.followingRepository = followingRepository;
        this.userRepository = userRepository;
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
    public Result getPersonalPostWall(long id) {
        //add both the post of id itself and its following users
        List<Following> followingList = followingRepository.findFollowedPeopleByID(id);
        List<Post> posts = new ArrayList<>(postRepository.findPost(id));
        for (Following f : followingList) posts.addAll(postRepository.findPost(f.getTarget()));
        Collections.sort(posts);
        for (Post p : posts) {
            User user = userRepository.findByID(p.getAuthorID());
            p.setAuthorName(user.getUserName());
        }
        return ok(new Gson().toJson(posts));
    }

    public Result publishPost() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("Post not created, expecting Json data");
            return badRequest("Post not created, expecting Json data");
        }

        // Parse JSON file
        String author = json.path("authorId").asText();
        String content = json.path("content").asText();

        try {
            long authorId = Long.parseLong(author);
            long time = System.currentTimeMillis();
            Post post = new Post(authorId, content, 0, time);
            postRepository.save(post);
            System.out.println("Post succesfully saved: " + post.getId());
            return created(new Gson().toJson(post.getId()));
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.out.println("Post not saved: " + content);
            return badRequest("Post not saved: " + content);
        }
    }

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
        return ok("post: " + postId + " is updated");
    }

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
        return ok("post: " + postId + " is deleted");
    }

}
