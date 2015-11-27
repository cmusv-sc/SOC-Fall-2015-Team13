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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String authorId;
    private String content;
    private int numOfLikes;
    private long timestamp;
    private String securtiry;

    public static String getADD_POST_CALL() {
        return ADD_POST_CALL;
    }

    private String authorName;

    private static final String ADD_POST_CALL = Constants.NEW_BACKEND + "post";
    private static final String GET_POST_WALL_CALL = Constants.NEW_BACKEND + "post/wall/";
    private static final String GET_POSTANDCOMMENT_CALL = Constants.NEW_BACKEND + "post/home/";
    private static final String DELETE_POST_CALL = Constants.NEW_BACKEND + "post/delete";
    private static final String UPDATE_POST_CALL = Constants.NEW_BACKEND + "post/update";
    private static final String SEARCH_POST_CALL = Constants.NEW_BACKEND + "search/post/";
    private static final String POPULAR_POST_CALL = Constants.NEW_BACKEND + "post/popular";

    public Post() {
    }

    public Post(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.numOfLikes = 0;
        this.timestamp = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getSecurtiry() {
        return securtiry;
    }

    public void setSecurtiry(String securtiry) {
        this.securtiry = securtiry;
    }

    /**
     * Create a new post
     *
     * @param jsonData
     * @return the response from the API server
     */
    public static JsonNode create(JsonNode jsonData) {
        return APICall.postAPI(ADD_POST_CALL, jsonData);
    }

    /**
     * Delete a post
     *
     * @param jsonData
     * @return the response from the API server
     */
    public static JsonNode delete(JsonNode jsonData) {
        return APICall.postAPI(DELETE_POST_CALL, jsonData);
    }

    /**
     * Update an existing post
     *
     * @param jsonData
     * @return the response from the API server
     */
    public static JsonNode update(JsonNode jsonData) {
        return APICall.postAPI(UPDATE_POST_CALL, jsonData);
    }

    /**
     * Get a user based on its username
     *
     * @return
     */

    public static List<PostAndComments> get(String id) {
        JsonNode json = APICall.callAPI(GET_POSTANDCOMMENT_CALL + id);
        List<PostAndComments> postAndComments = new ArrayList<PostAndComments>();
        for (JsonNode node : json) {
            Post p = new Post();
            JsonNode postNode = node.path("post");
            p.setId(postNode.path("id").asText());
            p.setContent(postNode.path("content").asText());
            p.setAuthorName(postNode.path("authorName").asText());
            p.setTimestamp(postNode.path("timeStamp").asLong());
            p.setAuthorId(postNode.path("authorID").asText());
            p.setNumOfLikes(postNode.path("likes").asInt());
            p.setSecurtiry(node.path("security").asText());
            JsonNode commentNodes = node.path("comments");
            List<Comment> comments = new ArrayList<Comment>();
            for (JsonNode commentNode : commentNodes) {
                Comment comment = new Comment();
                comment.setId(commentNode.path(0).asText());
                comment.setAuthorName(commentNode.path(1).asText());
                comment.setContent(commentNode.path(2).asText());
                comment.setPostId(commentNode.path(3).asText());
                comment.setCommenterId(commentNode.path(4).asLong());
                comment.setTimeStamp(commentNode.path(5).asLong());
                comments.add(comment);
            }

            postAndComments.add(new PostAndComments(p, comments));
        }
        return postAndComments;
    }

    public static List<PostAndComments> getWall(String id) {
        JsonNode json = APICall.callAPI(GET_POST_WALL_CALL + id);
        List<PostAndComments> postAndComments = new ArrayList<PostAndComments>();
        for (JsonNode node : json) {
            Post p = new Post();
            JsonNode postNode = node.path("post");
            p.setId(postNode.path("id").asText());
            p.setContent(postNode.path("content").asText());
            p.setAuthorName(postNode.path("authorName").asText());
            p.setTimestamp(postNode.path("timeStamp").asLong());
            p.setAuthorId(postNode.path("authorID").asText());
            p.setNumOfLikes(postNode.path("likes").asInt());
            p.setSecurtiry(node.path("security").asText());
            JsonNode commentNodes = node.path("comments");
            List<Comment> comments = new ArrayList<Comment>();
            for (JsonNode commentNode : commentNodes) {
                Comment comment = new Comment();
                comment.setId(commentNode.path(0).asText());
                comment.setAuthorName(commentNode.path(1).asText());
                comment.setContent(commentNode.path(2).asText());
                comment.setPostId(commentNode.path(3).asText());
                comment.setCommenterId(commentNode.path(4).asLong());
                comment.setTimeStamp(commentNode.path(5).asLong());
                comments.add(comment);
            }

            postAndComments.add(new PostAndComments(p, comments));
        }
        return postAndComments;
    }


    public static List<PostAndComments> search(String keyword) {
        JsonNode json = APICall.callAPI(SEARCH_POST_CALL + keyword);
        List<PostAndComments> postAndComments = new ArrayList<>();
        for (JsonNode node : json) {
            Post p = new Post();
            JsonNode postNode = node.path("post");
            p.setId(postNode.path("id").asText());
            p.setContent(postNode.path("content").asText());
            p.setAuthorName(postNode.path("authorName").asText());
            p.setTimestamp(postNode.path("timeStamp").asLong());
            p.setAuthorId(postNode.path("authorID").asText());
            p.setNumOfLikes(postNode.path("likes").asInt());
            p.setSecurtiry(node.path("security").asText());
            JsonNode commentNodes = node.get("comments");
            List<Comment> comments = new ArrayList<Comment>();
            for (JsonNode commentNode : commentNodes) {
                Comment comment = new Comment();
                comment.setId(commentNode.path(0).asText());
                comment.setAuthorName(commentNode.path(1).asText());
                comment.setContent(commentNode.path(2).asText());
                comment.setPostId(commentNode.path(3).asText());
                comment.setCommenterId(commentNode.path(4).asLong());
                comment.setTimeStamp(commentNode.path(5).asLong());
                comments.add(comment);
            }

            postAndComments.add(new PostAndComments(p, comments));
        }
        return postAndComments;
    }

    public static List<Post> getPopular() {
        JsonNode json = APICall.callAPI(POPULAR_POST_CALL);
        List<Post> posts = new ArrayList<>();
        for (JsonNode node : json) {
            Post p = new Post();
            p.setContent(node.path("content").asText());
            p.setAuthorName(node.path("authorName").asText());
            p.setTimestamp(node.path("timeStamp").asLong());
            p.setAuthorId(node.path("authorID").asText());
            p.setNumOfLikes(node.path("likes").asInt());
            p.setSecurtiry(node.path("security").asText());
            posts.add(p);
            System.out.println(posts);
        }
        return posts;
    }


    @Override
    public String toString() {
        return "Post [id=" + id + ", authorId=" + authorId + " authorName=" + authorName + ", content="
                + content + ", numOfLikes=" + numOfLikes + ", timestamp="
                + timestamp + "]";
    }
}
