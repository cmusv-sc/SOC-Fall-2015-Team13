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

import models.Post;
import models.PostAndComments;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import views.html.network.*;

import java.util.List;

public class MainController extends Controller {
    final static Form<User> userForm = Form.form(User.class);

    public static Result myHome() {
        String viewerId = null;
        try {
            viewerId = session("current_user");
        } catch (Exception e) {
            System.out.println("session error");
            e.printStackTrace();
        }
        if (viewerId == null || viewerId.isEmpty()) {
            return redirect("/login");
        }
        User user = User.get(viewerId);
        List<PostAndComments> postAndComments = Post.getMainWall(viewerId);
        List<User> users = User.getFollowers(viewerId);
        if (viewerId == null || viewerId.isEmpty()) {
            return redirect("/login");
        }
        int follow=1;
        for(User u : users){
            if(viewerId.equals(String.valueOf(u.getId()))){
                follow=0;
            }
        }
        return ok(main.render(user, userForm, users, viewerId, postAndComments, Post.getPopular(), follow));
    }
}
