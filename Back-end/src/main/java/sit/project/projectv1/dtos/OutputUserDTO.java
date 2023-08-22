package sit.project.projectv1.dtos;

import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Role;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OutputUserDTO {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private Role role;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
