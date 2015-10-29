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
import models.metadata.ClimateService;
import play.libs.Json;
import play.mvc.*;
import play.data.Form;
import util.APICall;
import views.html.network.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeController extends Controller {
    final static Form<User> userForm = Form
            .form(User.class);

    public static Result home(String username) {
        User user = User.get(username);
        return ok(home.render(user, userForm));
    }
}
