package sit.project.projectv1.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Role;
import sit.project.projectv1.exceptions.EnumSizeLimit;
import sit.project.projectv1.exceptions.UserUnique;

@Getter
@Setter
public class putUserDTO {

    @NotNull@NotBlank
    @UserUnique(username = true)
    @Size(min = 1, max = 45)
    private String username;

    @NotNull@NotBlank
    @UserUnique(name = true)
    @Size(min = 1, max = 100)
    private String name;

    @NotNull@NotBlank
    @UserUnique(email = true)
    @Email(message = "Email should be valid")
    @Size(min = 1, max = 150)
    private String email;

    @EnumSizeLimit(targetClassType = Role.class, message = "must be either 'announcer' or 'admin'")
    private String role;

    public Role getRole() {
        if (role == null) {
            return Role.announcer;
        }
        return Role.valueOf(role.trim());
    }
}
