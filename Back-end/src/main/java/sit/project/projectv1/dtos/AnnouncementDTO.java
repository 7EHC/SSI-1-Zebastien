package sit.project.projectv1.dtos;

import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.entities.Category;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AnnouncementDTO {
    private Integer id;
    private String announcementTitle;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private Display announcementDisplay;

    private Category announcementCategory;

    private User announcementOwner;

    public String getAnnouncementCategory() {
        return announcementCategory == null? null : announcementCategory.getCategoryName();
    }

    public String getAnnouncementOwner() {
        return announcementOwner == null ? null : announcementOwner.getUsername();
    }

}
