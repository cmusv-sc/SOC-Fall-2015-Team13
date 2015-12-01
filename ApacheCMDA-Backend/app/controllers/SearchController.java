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

import com.google.gson.Gson;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import search.PostSearch;
import search.UserSearch;
import search.SearchMode;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class SearchController extends Controller {

    private UserSearch searchUser = new UserSearch();
    private PostSearch searchPost = new PostSearch();
    private UserRepository userRepository;
    private PostRepository postRepository;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public SearchController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public void appendPost(long id, String content) {
        searchPost.appendPost(id, content);
    }

    public void deletePost(long id) {
        searchPost.deletePost(id);
    }

    public void updatePost(long id, String content) {
        deletePost(id);
        appendPost(id, content);
    }

    public void appendUser(long id, User user){
        searchUser.appendUser(id, user);
    }

    public void updateUser(long id, User user){
        searchUser.updateUser(id, user);
    }

    public Result searchPost(String viewerID, String keyword) {
        List<PostAndComment> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        try {
            ids = searchPost.basicSearch(parse(keyword), SearchMode.EXACTLY_MATCH, "content");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String id : ids) {
            List<Post> posts = postRepository.findPostByPostID(Long.valueOf(id));
            List<PostAndComment> postAndComments = new ArrayList<>();
            for (Post p : posts) {
                String security = p.getSecurity();
                if (security != null && (p.getAuthorID()!=Long.parseLong(viewerID)&& p.getSecurity().equals("private"))) continue;
                User user = userRepository.findByID(p.getAuthorID());
                p.setAuthorName(user.getUserName());
                List<Comment> comments = postRepository.findComment(p.getId());
                postAndComments.add(new PostAndComment(p, comments));
            }
            result.addAll(postAndComments);
        }
        return ok(new Gson().toJson(result));
    }

    public Result defaultSearch(String keyword) {
        List<User> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        try {
            ids = searchUser.basicSearch(parse(keyword), SearchMode.FUZZY, "default");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String id : ids) result.add(userRepository.findByID(Long.valueOf(id)));
        return ok(new Gson().toJson(result));
    }

    public Result fuzzySearch(String keyword, String field) {
        keyword = keyword.replace("_", " ");
        List<User> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        try {
            ids = searchUser.basicSearch(parse(keyword), SearchMode.FUZZY, field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String id : ids) result.add(userRepository.findByID(Long.valueOf(id)));
        return ok(new Gson().toJson(result));
    }


    public Result exactlySearch(String keyword, String field) {
        List<User> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        try {
            ids = searchUser.basicSearch(parse(keyword), SearchMode.EXACTLY_MATCH, field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String id : ids) result.add(userRepository.findByID(Long.valueOf(id)));
        return ok(new Gson().toJson(result));
    }

    private String parse(String keyword) {
        return keyword.replace("_", " ").replace("!", "");
    }

}
