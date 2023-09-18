package sit.project.projectv1.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
    @Getter
    @Setter
    public class InputUserLoginDTO {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

