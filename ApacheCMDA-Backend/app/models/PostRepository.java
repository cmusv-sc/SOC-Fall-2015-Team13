package models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by bluebyte60 on 11/3/15.
 */
@Named
@Singleton
public interface PostRepository extends CrudRepository<Post, Long> {
    @Query(value = "select p.* from Post p where p.authorID=? order by p.timeStamp desc", nativeQuery = true)
    List<Post> findPost(long id);

}
