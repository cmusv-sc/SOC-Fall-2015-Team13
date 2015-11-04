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

    private long source;

    private long target;

    public Following(long source, long target) {
        this.source = source;
        this.target = target;
    }

    public Following() {
    }

    public long getSource() {
        return source;
    }

    public void setSource(long source) {
        this.source = source;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }
}
