package sit.project.projectv1.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Role;
import sit.project.projectv1.exceptions.EnumSizeLimit;

import java.time.ZonedDateTime;

@Getter
@Setter

public class InputUserDTO {
    @NotBlank
    @Size(min = 1, max = 45)
    private String username;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Size(min = 1, max = 100)
    private String email;

    @EnumSizeLimit(targetClassType = Role.class, message = "must be either 'announcer' or 'admin'")
    private String role;

    public Role getRole() {
        if (role == null) {
            return Role.announcer;
        }
        return Role.valueOf(role);
    }

    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
