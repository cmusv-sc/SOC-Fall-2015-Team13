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
public interface KeywordRepository extends CrudRepository<Keyword, Long> {

    @Query(value = "select k.id, k.keyword from Keyword k group by k.keyword having (count(k.keyword)>=3)  order by (count(k.keyword)>=3) desc ", nativeQuery = true)
    List<Keyword> recommendation();
}
