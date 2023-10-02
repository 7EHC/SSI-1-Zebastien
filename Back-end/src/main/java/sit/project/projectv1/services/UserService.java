package sit.project.projectv1.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.dtos.InputUserLoginDTO;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.repositories.UserRepository;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Argon2PasswordEncoder argon2;

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


    public Boolean matchPassword(InputUserLoginDTO input){
        if(userRepository.existsByUsername(input.getUsername())){
            String inputPassword = input.getPassword();
            String existPassword = userRepository.findByUsername(input.getUsername()).getPassword();
            if(argon2.matches(inputPassword,existPassword) == true){
                return argon2.matches(inputPassword,existPassword);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

//    public ResponseEntity<Map<String, String>> authenticate(String username, String rawPassword) {
//        if (userRepository.existsByUsername(username)) {
//            User user = userRepository.findByUsername(username);
//
//            if (argon2.matches(rawPassword, user.getPassword())) {
//                String accessToken = generateJwtToken(username, accessTokenExpiration);
//                String refreshToken = generateJwtToken(username, refreshTokenExpiration);
//
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("accessToken", accessToken);
//                tokens.put("refreshToken", refreshToken);
//
//                return ResponseEntity.ok(tokens);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid password"));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
//        }
//    }
//
//    private String generateJwtToken(String username, int expiration) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//    }

    }
