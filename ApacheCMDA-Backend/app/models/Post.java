package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class Post implements Comparable<Post> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String author;

    private String content;

    private int likes;

    private long timeStamp;

    public Post(String author, String content, int likes, long timeStamp) {
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.timeStamp = timeStamp;
    }

    public Post() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(Post another) {
        if (this.timeStamp < another.timeStamp) return 1;
        else if (this.timeStamp > another.timeStamp) return -1;
        else return 0;
    }
}
