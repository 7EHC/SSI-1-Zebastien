package sit.project.projectv1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.project.projectv1.entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, EntityRepository<User> {
        boolean existsByUsername(String username);
        boolean existsByName(String name);
        boolean existsByEmail(String email);
        User findByUsername(String username);




    }

