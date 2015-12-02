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

    @Query(value = "select p.* from Post p  order by p.timeStamp  desc limit 100", nativeQuery = true)
    List<Post> findPopularPost();

    @Query(value = "select c.id, c.authorName, c.content, c.postId, c.commenterId, c.timeStamp from Comment c where c.postId=? order by c.timeStamp asc", nativeQuery = true)
    List<Comment> findComment(long id);

    @Query(value = "select ifnull(max(p.id), 0) from Post p  order by p.id desc limit 1", nativeQuery = true)
    long latestID();

    @Query(value = "select distinct p.* from Post p JOIN (select s.postId as pid from Share s where s.sharerId=?) s on s.pid = p.id", nativeQuery = true)
    List<Post> findSharedPost(long id);
}
