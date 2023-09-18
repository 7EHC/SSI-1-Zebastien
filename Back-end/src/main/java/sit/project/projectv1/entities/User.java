package sit.project.projectv1.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Role;
import sit.project.projectv1.exceptions.UserUnique;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Integer id;

    @NotBlank@NotNull
    @UserUnique(username = true)
    @Column(name ="username")
    @Size(min = 1, max = 45)
    private String username;

    @NotBlank@NotNull
    @UserUnique(name=true)
    @Column(name ="name")
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank@NotNull
    @UserUnique(email = true)
    @Column(name ="email" )
    @Email(message = "Email should be valid")
    @Size(min = 1, max = 150)
    private String email;

    @Column(name ="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @Column(name ="password")
    private String password;


    @Column(name ="createdOn", insertable = false , updatable = false )
    private ZonedDateTime createdOn;

    @Column(name ="updatedOn", insertable = false , updatable = false )
    private ZonedDateTime updatedOn;


}
