package sit.project.projectv1.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Autowired
    private JwtRequestFilter requestFilter;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors();
        http
                .csrf().disable().authorizeHttpRequests().requestMatchers("/api/token","/api/announcements","api/announcements/pages","api/categories","api/users/match").permitAll()
                .anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(entryPoint).and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//        http
//                .cors().and().csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(entryPoint).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers(OPTIONS).permitAll() // allow CORS option calls for Swagger UI
//                        .requestMatchers("/api/token","api/categories","api/match").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/api/announcements/pages").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/api/categories").permitAll()
////                        .requestMatchers(HttpMethod.GET,"/api/users").permitAll()
//                        .requestMatchers(HttpMethod.POST,"/api/match").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/api/announcements/{id:\\d+}").permitAll()
//                        .anyRequest().authenticated()).addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
//                .httpBasic();
//
//        return http.build();
//    }
}
