package sit.project.projectv1.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Size(min = 8,max = 14, message = "size must be between 8 and 14")
    @Pattern(message = "must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters",
            regexp = "^(?=.?[A-Z])(?=.?[a-z])(?=.?[0-9])(?=.?[#?!@$%^&*-]).{8,14}$")
    private String password;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Size(min = 1, max = 100)
    @Email(message = "Email should be valid", regexp = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)$")
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
