package sit.project.projectv1.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.advice.ErrorResponse;
import sit.project.projectv1.dtos.InputUserDTO;
import sit.project.projectv1.dtos.InputUserLoginDTO;
import sit.project.projectv1.dtos.OutputUserDTO;
import sit.project.projectv1.dtos.putUserDTO;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.enums.Role;
import sit.project.projectv1.services.UserService;
import sit.project.projectv1.utils.ListMapper;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")

public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    private List<User> getAllUser(){
        return userService.getAllUsers();
    }

    @PostMapping("")
    public User createUser(@RequestBody @Valid InputUserDTO userDTO) {
        userDTO.setUsername(userDTO.getUsername().trim());
        userDTO.setName(userDTO.getName().trim());
        userDTO.setEmail(userDTO.getEmail().trim());
        User user = modelMapper.map(userDTO, User.class);
        user.setId(null);
        return userService.createNewUser(user);
    }
    @PostMapping("/match")
    public Boolean matchPassword(@RequestBody InputUserLoginDTO inputUserLoginDTO){
        return userService.matchPassword(inputUserLoginDTO);
    }

//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable Integer id) {
//        userService.deleteUser(id);
//    }
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable Integer id, HttpServletRequest request) {
//        userService.deleteUser(id, request);
//    }
@DeleteMapping("/{userId}")
public void deleteUser(@PathVariable Integer userId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userService.getUserFromToken(authentication);

    if (user.getRole() == Role.admin && userService.getUserById(userId) != user) {
        userService.deleteUser(userId, user);
    } else {
        throw new AccessDeniedException("Access denied!!!");
    }
}
    @GetMapping("/{id}")
    public OutputUserDTO getUserById(@PathVariable Integer id){
        return modelMapper.map(userService.getUserById(id), OutputUserDTO.class);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Integer id, @RequestBody @Valid putUserDTO updateUser) {
        User user = modelMapper.map(updateUser, User.class);
        return userService.updateUser(id, user);
    }

//    @PostMapping("/token")
//    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody InputUserLoginDTO inputUserLoginDTO) {
//        return userService.authenticate(inputUserLoginDTO.getUsername(), inputUserLoginDTO.getPassword());
//    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Entity attributes validation failed!", request.getDescription(false));
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            er.addValidationError(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
}
