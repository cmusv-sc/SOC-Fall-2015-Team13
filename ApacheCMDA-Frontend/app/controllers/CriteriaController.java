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
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

import views.html.network.*;

public class CriteriaController extends Controller {
    final static Form<User> userForm = Form.form(User.class);

    public static Result home(String id) {
        User user = User.get(id);
        List<Post> posts = Post.getWall(id);
        for (Post p : posts) System.out.println(p);
        List<User> users = User.getFollowers(id);
        String viewerId = session("current_user");
        int follow = 1;
        for (User u : users) {
            if (viewerId.equals(String.valueOf(u.getId()))) {
                follow = 0;
            }
        }
        return ok(criteria.render(user, userForm, users, viewerId, posts, follow));
    }


    public static Result search(String id) {
        User user = User.get(id);
        List<Post> posts = Post.getWall(id);
        for (Post p : posts) System.out.println(p);
        List<User> users = User.getFollowers(id);
        String viewerId = session("current_user");
        int follow = 1;
        for (User u : users) {
            if (viewerId.equals(String.valueOf(u.getId()))) {
                follow = 0;
            }
        }
        List<User> searchResult = new ArrayList<>();
        Form<User> dc = userForm.bindFromRequest();
        String firstName = dc.field("firstName").value();
        String lastName = dc.field("lastName").value();
        String reseachField = dc.field("researchFields").value();
        String enableFuzzy = dc.field("fuzzy").value();
        String keyword = dc.field("srch-term").value();
        if (keyword == null) return ok(searchUser.render(user, userForm, users, viewerId, searchResult, follow));

        Map<User, Integer> map = new HashMap<>();

        boolean isFuzzy = enableFuzzy == null ? false : true;

        if (firstName != null) put(map, User.search(keyword, "firstName", isFuzzy));
        if (lastName != null) put(map, User.search(keyword, "lastName", isFuzzy));
        if (reseachField != null) put(map, User.search(keyword, "researchFields", isFuzzy));

        SortedSet<Map.Entry<User, Integer>> r = entriesSortedByValues(map);

        for(Map.Entry<User, Integer> entry : r){
            searchResult.add(entry.getKey());
            System.out.println("entryentryentryentry"+entry.getKey());
            System.out.println("valuevaluevaluevalue"+entry.getValue());
        }

        return ok(searchUser.render(user, userForm, users, viewerId, searchResult, follow));
    }

    private static void put(Map<User, Integer> map, List<User> users) {
        for (User user : users) {
            if (map.containsKey(user)) map.put(user, map.get(user) + 1);
            else map.put(user, 1);
        }
    }

    static <K, V extends Comparable<? super V>>
    SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        if (res > 0) return -1;
                        else return 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
