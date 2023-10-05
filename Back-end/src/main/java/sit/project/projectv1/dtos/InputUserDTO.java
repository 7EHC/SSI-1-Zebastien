package sit.project.projectv1.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import sit.project.projectv1.enums.Role;
import sit.project.projectv1.exceptions.EnumSizeLimit;
import sit.project.projectv1.exceptions.UserUnique;

import java.time.ZonedDateTime;

@Getter
@Setter
public class InputUserDTO {

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

    @NotBlank
    @UserUnique(password = true)
    @Pattern(message = "must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters",
             regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#?_!@$%^&*=+-]).*$")
    @Size(min = 8,max = 14, message = "size must be between 8 and 14")
    private String password;


    @EnumSizeLimit(targetClassType = Role.class, message = "must be either 'announcer' or 'admin'")
    private String role;

    public Role getRole() {
        if (role == null) {
            return Role.announcer;
        }
        return Role.valueOf(role.trim());
    }
}
