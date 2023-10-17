package talkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import talkhub.model.Comment;
import talkhub.model.Topic;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c where SIZE(c.complaints)!=0 ORDER BY SIZE(c.complaints) DESC")
    List<Comment> findAllByOrderByComplaintsCount();
}
