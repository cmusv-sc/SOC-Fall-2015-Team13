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
import models.Following;
import models.FollowingRepository;
import models.Post;
import models.PostRepository;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class PostController extends Controller {

    private final PostRepository postRepository;
    private final FollowingRepository followingRepository;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public PostController(PostRepository postRepository, FollowingRepository followingRepository) {
        this.postRepository = postRepository;
        this.followingRepository = followingRepository;
    }


    public Result getPost(long id) {

        List<Post> result = postRepository.findPost(id);

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
        return ok(new Gson().toJson(posts));
    }

}
