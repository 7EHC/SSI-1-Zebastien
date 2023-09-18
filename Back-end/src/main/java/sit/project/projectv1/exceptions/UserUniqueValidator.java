package sit.project.projectv1.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sit.project.projectv1.repositories.UserRepository;
import sit.project.projectv1.services.UserService;


public class UserUniqueValidator implements ConstraintValidator<UserUnique, String> {


    public UserRepository userRepository;
    public UserUniqueValidator(UserRepository user){
     this.userRepository = user;
    }

    private boolean username;
    private boolean name;
    private boolean email;

    @Override
    public void initialize(UserUnique constraintAnnotation) {
        this.username = constraintAnnotation.username();
        this.name = constraintAnnotation.name();
        this.email = constraintAnnotation.email();
    }


    @Override
    public boolean isValid(String value,ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must not be blank").addConstraintViolation();
            return true;
        }


        if(this.username == true) {
            boolean NotUniqueUsername = userRepository.existsByUsername(value);
            if(NotUniqueUsername) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                return false;
            } else return true;
        }
        else if(this.name == true){
            boolean NotUniqueName = userRepository.existsByName(value);
            if(NotUniqueName){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                return false;
            } else return true;
        }
        else {
            boolean NotUniqueEmail = userRepository.existsByEmail(value);
            if(NotUniqueEmail){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                return false;
            } else return true;
        }







//        if (username == null){
//            return true;
//        }
//
//        boolean isNotUsernameUnique = userService.checkUniqueUsername(username);
//        boolean isNotNameUnique = userService.checkUniqueName(name);
//
//
//        if (isNotUsernameUnique || isNotNameUnique) {
//          context.disableDefaultConstraintViolation(); // ja pai disable default message exception
//            if (isNotUsernameUnique) {
//                context.buildConstraintViolationWithTemplate("Username is not unique").addPropertyNode("username").addConstraintViolation();
//            }
//            if (isNotNamelUnique) {
//                context.buildConstraintViolationWithTemplate("name is not unique").addPropertyNode("name").addConstraintViolation();
//            }
//            return false; //
//        }
//        return true;
    }
}


