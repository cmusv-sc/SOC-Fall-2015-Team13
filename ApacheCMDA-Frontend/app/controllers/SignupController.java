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
import play.libs.Json;
import play.mvc.*;
import play.data.Form;
import util.APICall;
import views.html.network.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupController extends Controller {
    final static Form<User> userForm = Form
            .form(User.class);

    public static Result signup() {
        return ok(signup.render());
    }

    public static Result newUser() {
        Form<User> dc = userForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        String id = "";
        try {
            jsonData.put("username", dc.field("username").value());
            jsonData.put("password", dc.field("password").value());
            jsonData.put("firstName", dc.field("firstName").value());
            jsonData.put("lastName", dc.field("lastName").value());
            jsonData.put("affiliation", dc.field("affiliation").value());
            JsonNode response = User.create(jsonData);
            System.out.println("user created with response: " + response);
            id = response.toString();
            Application.flashMsg(response);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(APICall.ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.UNKNOWN));
        }
//        return redirect("/network/home/" + dc.field("username").value());
        return redirect("/network/home/" + id);
    }
}
