package sit.project.projectv1.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.config.JwtTokenUtil;
import sit.project.projectv1.dtos.InputUserLoginDTO;
import sit.project.projectv1.entities.Announcement;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.enums.Role;
import sit.project.projectv1.exceptions.ResourceNotFoundException;
import sit.project.projectv1.repositories.AnnouncementRepository;
import sit.project.projectv1.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Argon2PasswordEncoder argon2;
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${jwt.expiration.access}")
//    private int accessTokenExpiration;  // In seconds
//
//    @Value("${jwt.expiration.refresh}")
//    private int refreshTokenExpiration;  // In seconds


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + id + " does not exist"));
    }

    public User createNewUser(User user){
        String hashedCode = argon2.encode(user.getPassword());
        user.setPassword(hashedCode);
        User usr = userRepository.saveAndFlush(user);
        userRepository.refresh(usr);
        return usr;
    }

//    public void deleteUser(Integer id) {
//        if (userRepository.existsById(id)) {
//            userRepository.deleteById(id);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This USER does not exist!!!");
//        }
//    }

//    public void deleteUser(Integer id, HttpServletRequest request) {
//        String token = request.getHeader("Authorization").substring(7);
//        String adminUsername = jwtTokenUtil.getAdminUsernameFromToken(token);
//        Optional<User> userOptional = userRepository.findById(id);
//        if (userOptional.isPresent()) {
//            User userToDelete = userOptional.get();
//
//            // Retrieve announcements owned by the user being deleted
//            List<Announcement> announcementsToDelete = announcementRepository.findAnnouncementByAnnouncementOwner(userToDelete);
//
//            // Set the admin user as the new owner for these announcements
//            User adminUser = userRepository.findByUsername(adminUsername);
//            if (adminUser == null) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found!!!");
//            }
//            for (Announcement announcement : announcementsToDelete) {
//                announcement.setAnnouncementOwner(adminUser);
//            }
//
//            // Save the updated announcements
//            announcementRepository.saveAll(announcementsToDelete);
//
//            // Delete the user
//            userRepository.deleteById(id);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This USER does not exist!!!");
//        }
//    }

    public void deleteUser(Integer userId, User user){
        User userToDelete = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This User does not exist!!"));

        // Retrieve announcements owned by the user being deleted
        List<Announcement> announcementsToDelete = announcementRepository.findAllByAnnouncementOwner(userToDelete);

        // Set the admin user as the new owner for these announcements
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "THIS USER DOES NOT EXIST!!");
        }
        for (Announcement announcement : announcementsToDelete) {
            announcement.setAnnouncementOwner(user);
        }

        // Save the updated announcements
        announcementRepository.saveAll(announcementsToDelete);

        // Delete the user
        userRepository.deleteById(userId);
    }


    public User updateUser(Integer id, User user) {
//        User usr = userRepository.findById(id).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + id + " does not exist"));
//        usr.setName(user.getName().trim());
//        usr.setUsername(user.getUsername().trim());
//        usr.setEmail(user.getEmail().trim());
//        usr.setRole(user.getRole());
//        User updateUsr =  userRepository.saveAndFlush(usr);
//        userRepository.refresh(updateUsr);
//        return updateUsr;

        User usr = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + id + " does not exist"));

        usr.setName(user.getName().trim());
        usr.setUsername(user.getUsername().trim());
        usr.setEmail(user.getEmail().trim());

        // Assuming getRole() returns a String, perform case-sensitive comparison
        String newRole = user.getRole().toString();

        // Check if the role exists in a case-sensitive manner
        if (Role.contains(newRole)) {
            usr.setRole(Role.valueOf(newRole));
        } else {
            // Handle the case where the role is not valid
            throw new IllegalArgumentException("Invalid role: " + newRole);
        }

        User updateUsr = userRepository.saveAndFlush(usr);
        userRepository.refresh(updateUsr);
        return updateUsr;
    }


    public Boolean matchPassword(InputUserLoginDTO input) {
        if (userRepository.existsByUsername(input.getUsername())) {
            String inputPassword = input.getPassword();
            String existPassword = userRepository.findByUsername(input.getUsername()).getPassword();
            if (argon2.matches(inputPassword, existPassword) == true) {
                return argon2.matches(inputPassword, existPassword);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            throw new ResourceNotFoundException("Username not found");
        }
    }
    public User getUserFromToken(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userRepository.findByUsername(username);
            return user;
        }
        return null;
    }
}
