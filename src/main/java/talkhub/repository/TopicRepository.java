package talkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import talkhub.model.Topic;
import talkhub.model.enums.Category;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("SELECT t FROM Topic t JOIN t.categories c WHERE c IN :categories GROUP BY t ORDER BY SIZE(t.comments) DESC")
    List<Topic> findAllByCategoriesInOrderByCommentsCount(@Param("categories") List<Category> categories);
    @Query("SELECT t FROM Topic t LEFT JOIN t.comments c GROUP BY t ORDER BY SIZE(t.comments) DESC")
    List<Topic> findAllByOrderByCommentsCount();
    List<Topic> findAllByCategoriesInOrderByDateDesc(List<Category> categories);
    List<Topic> findAllByOrderByDateDesc();
    List<Topic> findByNameIgnoreCaseContainingOrTextIgnoreCaseContaining(String name, String text);
    @Query("SELECT t FROM Topic t where SIZE(t.complaints)!=0 ORDER BY SIZE(t.complaints) DESC")
    List<Topic> findAllByOrderByComplaintsCount();
}
