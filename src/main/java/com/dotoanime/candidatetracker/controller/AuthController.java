package com.dotoanime.candidatetracker.controller;

import com.dotoanime.candidatetracker.exception.AppException;
import com.dotoanime.candidatetracker.model.Role;
import com.dotoanime.candidatetracker.model.RoleName;
import com.dotoanime.candidatetracker.model.User;
import com.dotoanime.candidatetracker.payload.ApiResponse;
import com.dotoanime.candidatetracker.payload.JwtAuthenticationResponse;
import com.dotoanime.candidatetracker.payload.LoginRequest;
import com.dotoanime.candidatetracker.payload.SignUpRequest;
import com.dotoanime.candidatetracker.repository.RoleRepository;
import com.dotoanime.candidatetracker.repository.UserRepository;
import com.dotoanime.candidatetracker.security.CustomUserDetailsService;
import com.dotoanime.candidatetracker.security.JwtTokenProvider;
import com.dotoanime.candidatetracker.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal user = (UserPrincipal) customUserDetailsService.loadUserByUsername(loginRequest.getUsername());

        String jwt = tokenProvider.generateToken(user);
        return ResponseEntity.ok(new JwtAuthenticationResponse(user.getUsername(), user.getName(), jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(signUpRequest.getUsername());

        String jwt = tokenProvider.generateToken(userPrincipal);

        return ResponseEntity.created(location).body(new JwtAuthenticationResponse(userPrincipal.getUsername(), userPrincipal.getName(), jwt));
    }
}
