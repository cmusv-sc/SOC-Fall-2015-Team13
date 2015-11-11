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
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Post;
import models.User;
import models.metadata.ClimateService;
import play.libs.Json;
import play.mvc.*;
import play.data.Form;
import util.APICall;
import views.html.network.*;

import java.util.ArrayList;
import java.util.List;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeController extends Controller {
    final static Form<User> userForm = Form.form(User.class);

    public static Result home(String id) {
        User user = User.get(id);
        List<Post> posts = Post.get(id);
        for (Post p : posts) System.out.println(p);
        List<User> users = User.getFollowers(id);
        return ok(home.render(user, userForm, users, posts));
    }

    public static Result login() {
        Form<User> dc = userForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        User user = new User();
        try {
            jsonData.put("username", dc.field("username").value());
            jsonData.put("password", dc.field("password").value());
            user = User.verifyAndGet(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.CONVERSIONERROR));
        } catch (NumberFormatException e) {
            user.setUserName(dc.field("username").value());
            user.setPassword(dc.field("password").value());
//            return ok(login.render(user, userForm, e.getMessage()));
            return redirect(controllers.routes.LoginController.login(user.getUserName(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.UNKNOWN));
        }
        List<User> users = User.getFollowers(String.valueOf(user.getId()));
        return ok(home.render(user, userForm, users, Post.get(String.valueOf(user.getId()))));
    }

    public static Result followers(String id) {
        List<User> users = User.getFollowers(id);
        return ok(followers.render(users));
    }

}
