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
package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;
import util.APICall;
import util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PostAndComments {
    private Post post;
    private List<Comment> comments;

    public void setPost(Post post) {
        this.post = post;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public PostAndComments(){

    }

    public PostAndComments(Post post, List<Comment> comments) {
        this.post = post;
        this.comments = comments;
    }

    public Post getPost() {
        return post;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
