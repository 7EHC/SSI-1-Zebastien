package sit.project.projectv1.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sit.project.projectv1.entities.Announcement;
import sit.project.projectv1.entities.Category;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.enums.Display;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    Page<Announcement> findAllByAnnouncementCategory(Pageable pageable, Category category);
    List<Announcement> findAllByAnnouncementCategory(Category category);

    List<Announcement> findAllByAnnouncementOwner(User user);
    List<Announcement> findAnnouncementByAnnouncementOwner(User owner);
    List<Announcement> findAllByAnnouncementDisplay(Display display);
}
