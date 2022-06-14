package cocktail.api;

import org.springframework.data.jpa.repository.JpaRepository;
import cocktail.domain.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
}

