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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Id;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;
    @ManyToOne(optional = false)
    @JoinColumn(name = "commenterId", referencedColumnName = "id")
    private User commenter;

    public String getAuthorName() {
        return authorName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String authorName;
    private String content;
    private long timeStamp;

    public Comment(Post post, User commenter, String content, String authorName, long timeStamp) {
        this.post = post;
        this.commenter = commenter;
        this.content = content;
        this.authorName = authorName;
        this.timeStamp = timeStamp;
    }

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getCommenter() {
        return commenter;
    }

    public String getContent() {
        return content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
