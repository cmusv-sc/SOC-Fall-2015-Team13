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
import play.data.validation.Constraints;
import util.APICall;
import util.Constants;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String authorId;
    private String content;
    private int numOfLikes;
    private long timestamp;

    private static final String ADD_POST_CALL = Constants.NEW_BACKEND + "post";

    public Post(){
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


    /**
     * Create a new user
     *
     * @param jsonData
     * @return the response from the API server
     */
    public static JsonNode create(JsonNode jsonData) {
        return APICall.postAPI(ADD_POST_CALL, jsonData);
    }

    @Override
    public String toString() {
        return "Post [id=" + id + ", authorId=" + authorId + ", content="
                + content + ", numOfLikes=" + numOfLikes + ", timestamp="
                + timestamp + "]";
    }
}
