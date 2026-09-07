package com.petitionvoice.backend.services;

import com.petitionvoice.backend.dto.User.LoginRequest;
import com.petitionvoice.backend.dto.User.UserRegistrationRequest;
import com.petitionvoice.backend.dto.User.UserResponse;
import com.petitionvoice.backend.enums.Role;
import com.petitionvoice.backend.mapper.UserMapper;
import com.petitionvoice.backend.model.User;
import com.petitionvoice.backend.model.UserDetails;
import com.petitionvoice.backend.repository.UserRepository;
import com.petitionvoice.backend.security.SecurityUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserMapper userMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponse register(UserRegistrationRequest input) {
        User user = new User();
        UserDetails userDetails = new UserDetails();

        user.setFirst_name(input.getFirstName());
        user.setLast_name(input.getLastName());

        userDetails.setEmail(input.getEmail());
        userDetails.setTelephone(input.getTelephone());
        userDetails.setPassword_hash(passwordEncoder.encode(input.getPassword()));

        user.setRole(Role.REGISTERED_USER);

        user.setUserDetails(userDetails);
        userDetails.setUser(user);

        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    public String login(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        User authenticatedUser = userRepository.findByUserDetails_Email(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SecurityUser securityUser = new SecurityUser(authenticatedUser);
        return jwtService.generateToken(securityUser);
    }
}