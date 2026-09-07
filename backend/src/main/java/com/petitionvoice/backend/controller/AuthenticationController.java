package com.petitionvoice.backend.controller;

import com.petitionvoice.backend.dto.User.LoginRequest;
import com.petitionvoice.backend.dto.User.UserRegistrationRequest;
import com.petitionvoice.backend.dto.User.UserResponse;
import com.petitionvoice.backend.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/accounts")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRegistrationRequest registerUserDto
    ) {
        UserResponse response = authenticationService.register(registerUserDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(
            @Valid @RequestBody LoginRequest loginUserDto
    ) {
        String jwtToken = authenticationService.login(loginUserDto);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
}