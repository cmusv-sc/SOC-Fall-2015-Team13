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
import models.User;
import models.Post;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.APICall;

public class PostController extends Controller {
    final static Form<User> userForm = Form.form(User.class);
    final static Form<Post> postForm = Form.form(Post.class);
    public static Result newPost() {
        Form<Post> dc = postForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        String id = "";
        try {
            jsonData.put("authorId", dc.field("authorId").value());
            id = dc.field("authorId").value();
            jsonData.put("content", dc.field("postContent").value());
            long currentTime = System.currentTimeMillis();
            jsonData.put("timestamp", currentTime);
            JsonNode response = Post.create(jsonData);
            System.out.println("post created with response: " + response);
            Application.flashMsg(response);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(APICall.ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.UNKNOWN));
        }
        return redirect("/network/home/" + id);
    }
}
