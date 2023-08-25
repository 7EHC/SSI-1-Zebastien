package sit.project.projectv1.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Role;

import java.time.ZoneOffset;
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

    @Column(name ="username")
    private String username;

    @Column(name ="name")
    private String name;

    @Column(name ="email")
    private String email;

    @Column(name ="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name ="createdOn", insertable = false , updatable = false )
    private ZonedDateTime createdOn;

    @Column(name ="updatedOn", insertable = false , updatable = false )
    private ZonedDateTime updatedOn;
}
