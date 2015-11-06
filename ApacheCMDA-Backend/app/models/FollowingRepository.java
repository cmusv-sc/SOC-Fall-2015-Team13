package models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * Created by bluebyte60 on 11/3/15.
 */
@Named
@Singleton
public interface FollowingRepository extends CrudRepository<Following, Long> {
    @Query(value = "select f.* from Following f where f.source=?", nativeQuery = true)
    List<Following> findFollowedPeopleByID(long source);



}
