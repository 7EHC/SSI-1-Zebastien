package sit.project.projectv1.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.repositories.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Argon2PasswordEncoder argon2;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        }
        else {
            throw new UsernameNotFoundException("User " + username + " Does not EXIST!!");
        }
    }

    public Boolean checkPassword(String username, String password){
        if(userRepository.existsByUsername(username)){
            String existPassword = userRepository.findByUsername(username).getPassword();
            if(argon2.matches(password,existPassword) == true){
                return argon2.matches(password,existPassword);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}