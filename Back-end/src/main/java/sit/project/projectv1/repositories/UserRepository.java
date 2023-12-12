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
        @Query("select u.username from User u")
        List<String> findUsername();
        @Query("select u.name from User u")
        List<String> findName();
        @Query("select u.email from User u")
        List<String> findEmail();




    }

