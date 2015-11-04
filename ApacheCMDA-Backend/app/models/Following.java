package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Following {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String source;

    private String target;

    public Following(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public Following() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
