package sit.project.projectv1.controllers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.dtos.AddAnnouncementDTO;
import sit.project.projectv1.dtos.InputUserDTO;
import sit.project.projectv1.dtos.OutputAnnouncementDTO;
import sit.project.projectv1.dtos.OutputUserDTO;
import sit.project.projectv1.entities.Announcement;
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

    @PostMapping
    public OutputUserDTO createUser(@Valid @RequestBody InputUserDTO userDTO) {

        userDTO.setUsername(userDTO.getUsername().trim());
        userDTO.setName(userDTO.getName().trim());
        userDTO.setEmail(userDTO.getEmail().trim());
//        userDTO.setRole(userDTO.getRole().toString().trim());

        User user = modelMapper.map(userDTO, User.class);
        user.setId(null);
        userService.createNewUser(user);
        return modelMapper.map(user, OutputUserDTO.class);
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
    public OutputUserDTO update(@PathVariable Integer id, @RequestBody InputUserDTO updateUser) {
        User user = modelMapper.map(updateUser, User.class);
        user.setId(id);
        userService.updateUser(id, user);
        return modelMapper.map(user, OutputUserDTO.class);
    }
}
