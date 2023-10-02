package sit.project.projectv1.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.config.JwtTokenUtil;
import sit.project.projectv1.dtos.JwtRefreshResponse;
import sit.project.projectv1.dtos.JwtRequest;
import sit.project.projectv1.dtos.JwtResponse;
import sit.project.projectv1.repositories.UserRepository;
import sit.project.projectv1.services.JwtUserDetailsService;


@RestController
@CrossOrigin
@RequestMapping(value = "/api/token")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Argon2PasswordEncoder argon2;
    @Autowired
    private UserRepository userRepository;
//    @Value("${jwt.expiration.access}")
//    private int accessTokenExpiration;  // In seconds
//
//    @Value("${jwt.expiration.refresh}")
//    private int refreshTokenExpiration;  // In seconds
//    @Value("${jwt.secret}")
//    private String jwtSecret;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        if (userRepository.existsByUsername(authenticationRequest.getUsername())) {
            if (userDetailsService.checkPassword(authenticationRequest.getUsername(), authenticationRequest.getPassword())) {
                authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

                final UserDetails userDetails = userDetailsService
                        .loadUserByUsername(authenticationRequest.getUsername());

                final String token = jwtTokenUtil.generateToken(userDetails);

                final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

                return ResponseEntity.ok(new JwtResponse(token, refreshToken));
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping
    public ResponseEntity<?> createAuthenticationTokenByRefreshToken(HttpServletRequest request) throws Exception {
        String requestHeaderToken = request.getHeader("Authorization");
        String refreshToken = requestHeaderToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtRefreshResponse(token));
    }

}
