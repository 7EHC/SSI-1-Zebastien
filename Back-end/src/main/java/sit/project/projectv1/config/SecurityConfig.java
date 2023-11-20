package sit.project.projectv1.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/token", "api/announcements/**","api/announcements/pages",
                        "api/announcements/{announcementId}","api/categories","api/users/match", "api/users/**")
                .permitAll()
                .requestMatchers("/api/users","api/users/match").hasRole("ADMIN")
                .requestMatchers("/api/announcements/**").hasRole("ANNOUNCER")
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(entryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
