package sit.project.projectv1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.project.projectv1.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
