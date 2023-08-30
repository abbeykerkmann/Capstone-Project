package seg4910.group17.capstoneserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seg4910.group17.capstoneserver.dao.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}
