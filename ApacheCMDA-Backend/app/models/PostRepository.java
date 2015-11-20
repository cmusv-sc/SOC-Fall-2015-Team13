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

    @Query(value = "select p.* from Post p where p.id=? order by p.timeStamp desc", nativeQuery = true)
    List<Post> findPostByPostID(long id);


    @Query(value = "select p.* from Post p  order by p.timeStamp  desc limit 10", nativeQuery = true)
    List<Post> findPopularPost();

    @Query(value = "select c.* from Comment c where c.postId=? order by c.timeStamp asc", nativeQuery = true)
    List<Comment> findComment(long id);
}
