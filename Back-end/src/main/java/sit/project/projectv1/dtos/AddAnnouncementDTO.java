package sit.project.projectv1.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Display;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AddAnnouncementDTO {
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private String announcementDisplay;

    public Display getAnnouncementDisplay() {
        if (announcementDisplay == null) {
            return Display.N;
        }
        return Display.valueOf(announcementDisplay);
    }

    @NotNull
    private Integer categoryId;
}

