package talkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import talkhub.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
