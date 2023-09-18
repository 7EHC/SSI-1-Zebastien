package sit.project.projectv1.exceptions;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserUniqueValidator.class)
@Documented
public @interface UserUnique {
    String message() default "the field is not unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean name() default false;
    boolean username() default false;
    boolean email() default false;

}
