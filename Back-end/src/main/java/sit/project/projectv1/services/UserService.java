package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.repositories.UserRepository;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + id + " does not exist"));
    }

    public User createNewUser(User user){
        User usr = userRepository.saveAndFlush(user);
        userRepository.refresh(usr);
        return usr;
    }

    public void deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This USER does not exist!!!");
        }
    }

    public User updateUser(Integer id, User user) {
        User usr = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + id + " does not exist"));

        usr.setName(user.getName().trim());
        usr.setUsername(user.getUsername().trim());
        usr.setEmail(user.getEmail().trim());
        usr.setRole(user.getRole());
        User updateUsr =  userRepository.saveAndFlush(usr);
        userRepository.refresh(updateUsr);
        return updateUsr;
    }
}
