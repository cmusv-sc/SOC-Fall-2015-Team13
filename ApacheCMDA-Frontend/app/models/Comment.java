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

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long postId;
    private long commenterId;
    private String content;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    private String authorName;
    private long timeStamp;

    private static final String ADD_COMMENT_CALL = Constants.NEW_BACKEND + "comment";
    private static final String GET_COMMENT_CALL = Constants.NEW_BACKEND + "comment/";

    public Comment() {
    }

    public Comment(long postId, long commenterId, String content, String authorName, long timeStamp) {
        this.postId = postId;
        this.commenterId = commenterId;
        this.content = content;
        this.authorName = authorName;
        this.timeStamp = timeStamp;
    }

    public long getId() {
        return id;
    }

    public long getPostId() {
        return postId;
    }

    public long getCommenterId() {
        return commenterId;
    }

    public String getContent() {
        return content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setPostId(String postId) {
        this.postId = Long.parseLong(postId);
    }

    public void setCommenterId(long commenterId) {
        this.commenterId = commenterId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }


    /**
     * Create a new comment on an exiting post
     *
     * @param jsonData
     * @return the response from the API server
     */
    public static JsonNode create(JsonNode jsonData) {
        return APICall.postAPI(ADD_COMMENT_CALL, jsonData);
    }

//    /**
//     * Get a list of comments associated with the post
//     *
//     * @return
//     */
//
//    public static List<Comment> get(String id) {
//        JsonNode json = APICall.callAPI(GET_COMMENT_CALL + id);
//        List<Comment> comments = new ArrayList<>();
//        for (JsonNode node : json) {
//            Comment comment = new Comment();
//            comment.setId()
//            Post p = new Post();
//            p.setId(node.path("id").asText());
//            p.setContent(node.path("content").asText());
//            p.setAuthorName(node.path("authorName").asText());
//            p.setTimestamp(node.path("timeStamp").asLong());
//            p.setAuthorId(node.path("authorID").asText());
//            p.setNumOfLikes(node.path("likes").asInt());
//            posts.add(p);
//            System.out.println(posts);
//        }
//        return posts;
//    }
}
