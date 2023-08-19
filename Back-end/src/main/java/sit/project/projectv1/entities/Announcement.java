package sit.project.projectv1.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Display;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "Announcement")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcementID")
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;

    @Enumerated(EnumType.STRING)
    private Display announcementDisplay;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category announcementCategory;

    public ZonedDateTime getPublishDate() {
        return publishDate;
    }

    public ZonedDateTime getCloseDate() {
        return closeDate;
    }
}
