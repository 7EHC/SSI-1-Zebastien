package sit.project.projectv1;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import sit.project.projectv1.exceptions.UserUniqueValidator;
import sit.project.projectv1.repositories.UserRepository;
import sit.project.projectv1.services.UserService;
import sit.project.projectv1.utils.ListMapper;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ListMapper listMapper() {
        return ListMapper.getInstance();
    }

    @Bean
    public Argon2PasswordEncoder passwordEncode() {
        return new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
    }
    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserUniqueValidator userUniqueValidator(){
    return new UserUniqueValidator(userRepository);
    }
}
