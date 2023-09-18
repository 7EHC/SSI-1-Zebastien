package sit.project.projectv1.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import sit.project.projectv1.advice.ErrorResponse;
import sit.project.projectv1.dtos.*;
import sit.project.projectv1.entities.User;
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

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public OutputUserDTO getUserById(@PathVariable Integer id){
        return modelMapper.map(userService.getUserById(id), OutputUserDTO.class);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Integer id, @RequestBody @Valid InputUserDTO updateUser) {
        User user = modelMapper.map(updateUser, User.class);
        return userService.updateUser(id, user);
    }

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
