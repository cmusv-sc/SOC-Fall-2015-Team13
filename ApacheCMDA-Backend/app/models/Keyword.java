package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String keyword;

    public Keyword(String word) {
        this.keyword = word;
    }

    public Keyword() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
