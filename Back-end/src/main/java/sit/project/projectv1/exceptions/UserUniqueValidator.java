package sit.project.projectv1.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.repositories.UserRepository;

public class UserUniqueValidator implements ConstraintValidator<UserUnique, String> {

    @Autowired
    private HttpServletRequest request;
    public UserRepository userRepository;

    public UserUniqueValidator(UserRepository user) {
        this.userRepository = user;
    }

    private boolean username;
    private boolean name;
    private boolean email;
    private boolean password;

    @Override
    public void initialize(UserUnique constraintAnnotation) {
        this.username = constraintAnnotation.username();
        this.name = constraintAnnotation.name();
        this.email = constraintAnnotation.email();
        this.password = constraintAnnotation.password();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must not be blank").addConstraintViolation();
            return true;
        }

        if (request.getMethod().equals("PUT")) {
            String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String[] pathSplit = path.split("/");
            Integer userId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
            User storedUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exits!!!"));
            if (this.username && value.equals(storedUser.getUsername())) {
                return true;
            } else if (this.name && value.equals(storedUser.getName())) {
                return true;
            } else if (this.email && value.equals(storedUser.getEmail())) {
                return true;
            }

            // Check if the updated value already exists for other users
            if (this.username && userRepository.findUsername().contains(value)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                return false;
            } else if (this.name && userRepository.findUsername().contains(value)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                return false;
            } else if (this.email && userRepository.findUsername().contains(value)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                return false;
            }

            if (this.username == true) {
                boolean NotUniqueUsername = userRepository.findUsername().contains(value);
                if (NotUniqueUsername) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                    return false;
                } else return true;
            } else if (this.name == true) {
                boolean NotUniqueName = userRepository.findName().contains(value);
                if (NotUniqueName) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                    return false;
                } else return true;
            } else {
                boolean NotUniqueEmail = userRepository.findEmail().contains(value);
                if (NotUniqueEmail) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
                    return false;
                } else return true;
            }
        }
        if (this.username && userRepository.findUsername().contains(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
            return false;
        } else if (this.name && userRepository.findName().contains(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
            return false;
        } else if (this.email && userRepository.findEmail().contains(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("does not unique").addConstraintViolation();
            return false;
        }
        return true;
    }
}

//public class UserUniqueValidator implements ConstraintValidator<UserUnique, String> {
//
//    @Autowired
//    private HttpServletRequest request;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private boolean username;
//    private boolean name;
//    private boolean email;
//    private boolean password;
//
//    @Override
//    public void initialize(UserUnique constraintAnnotation) {
//        this.username = constraintAnnotation.username();
//        this.name = constraintAnnotation.name();
//        this.email = constraintAnnotation.email();
//        this.password = constraintAnnotation.password();
//    }
//
//    @Override
//    public boolean isValid(String value, ConstraintValidatorContext context) {
//        if (value == null || value.isEmpty()) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate("must not be blank").addConstraintViolation();
//            return true;
//        }
//
//        if (request.getMethod().equals("PUT")) {
//            return isValueUniqueDuringUpdate(value,context);
//        } else {
//            return isValueUnique(value,context);
//        }
//    }
//
//    private boolean isValueUnique(String value, ConstraintValidatorContext context) {
//        if (this.username && userRepository.existsByUsername(value)) {
//            addConstraintViolation(context, "does not unique");
//            return false;
//        } else if (this.name && userRepository.existsByName(value)) {
//            addConstraintViolation(context, "does not unique");
//            return false;
//        } else if (this.email && userRepository.existsByEmail(value)) {
//            addConstraintViolation(context, "does not unique");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean isValueUniqueDuringUpdate(String value, ConstraintValidatorContext context) {
//        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//        String[] pathSplit = path.split("/");
//        Integer userId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
//
//        User storedUser = userRepository.findById(userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist!!!"));
//
//        if (this.username && value.equals(storedUser.getUsername())) {
//            return true;
//        } else if (this.name && value.equals(storedUser.getName())) {
//            return true;
//        } else if (this.email && value.equals(storedUser.getEmail())) {
//            return true;
//        }
//
//        return isValueUnique(value, context);
//    }
//
//
//    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
//        context.disableDefaultConstraintViolation();
//        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
//    }
//}





