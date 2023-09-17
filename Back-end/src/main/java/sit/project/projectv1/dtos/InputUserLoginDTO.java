package sit.project.projectv1.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputUserLoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
