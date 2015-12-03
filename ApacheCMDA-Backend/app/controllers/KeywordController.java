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
import com.google.gson.Gson;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import search.PostSearch;
import search.SearchMode;
import search.UserSearch;

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
public class KeywordController extends Controller {

    private KeywordRepository keywordRepository;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public KeywordController(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public Result put(String keyword) {
        keyword = keyword.trim();
        if (keyword.length() == 0) return badRequest("empty keyword");
        keywordRepository.save(new Keyword(keyword.replace("_", " ")));
        return ok("keyword:" + keyword + " saved");
    }

    public Result recommendation() {
        return ok(new Gson().toJson(keywordRepository.recommendation()));
    }

}
