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

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.libs.Json;
import play.mvc.*;
import play.data.Form;
import views.html.network.*;


public class ProfileController extends Controller {
    final static Form<User> userForm = Form.form(User.class);

    public static Result myProfile() {
        String viewerId = null;
        try {
            viewerId = session("current_user");
        } catch (Exception e) {
            System.out.println("session error");
            e.printStackTrace();
        }
        User user = User.get(viewerId);
        return ok(profile.render(user, userForm));
    }

    public static Result get(String id) {
        User user = User.get(id);
        return ok(profile.render(user, userForm));
    }

    public static Result update(String id) {
        Form<User> dc = userForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        jsonData.put("firstName", dc.field("firstname").value());
        jsonData.put("lastName", dc.field("lastname").value());
        jsonData.put("affiliation", dc.field("affiliation").value());
        jsonData.put("title", dc.field("title").value());
        jsonData.put("phoneNumber", dc.field("phonenumber").value());
        jsonData.put("researchFields", dc.field("researchfields").value());
        jsonData.put("email", dc.field("email").value());
        jsonData.put("url", dc.field("url").value());
        User.update(id, jsonData);
        User user = User.get(id);
        return ok(profile.render(user, userForm));

    }

}
