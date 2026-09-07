package com.petitionvoice.backend.controller;

import com.petitionvoice.backend.dto.Activity.CodeVerificationRequest;
import com.petitionvoice.backend.dto.Activity.ConfirmationCodeRequest;
import com.petitionvoice.backend.dto.Activity.UserStatisticsResponse;
import com.petitionvoice.backend.dto.Petition.PetitionResponse;
import com.petitionvoice.backend.dto.User.UserResponse;
import com.petitionvoice.backend.services.UserActivityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "authorization")
public class UserActivityController {

    private final UserActivityService userActivityService;

    public UserActivityController(UserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }

    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(Authentication authentication) {
        String principal = authentication.getName(); // id-ul user-ului
        return ResponseEntity.ok(userActivityService.getUserDetails(principal));
    }

    @GetMapping("/{id}/signed-petitions")
    public ResponseEntity<List<PetitionResponse>> getSignedPetitions(@PathVariable Integer id) {
        return ResponseEntity.ok(userActivityService.getSignedPetitions(id));
    }

    @GetMapping("/{id}/submitted-petitions")
    public ResponseEntity<List<PetitionResponse>> getSubmittedPetitions(@PathVariable Integer id) {
        return ResponseEntity.ok(userActivityService.getSubmittedPetitions(id));
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<UserStatisticsResponse> getUserStatistics(@PathVariable Integer id) {
        return ResponseEntity.ok(userActivityService.getUserStatistics(id));
    }

    @PostMapping("/generate-confirmation-code")
    public ResponseEntity<String> generateConfirmationCode(@Valid @RequestBody ConfirmationCodeRequest request) {
        userActivityService.generateVerificationCode(request.getEmail());
        return ResponseEntity.ok("Confirmation code generated");
    }

    @PostMapping("/verify-confirmation-code")
    public ResponseEntity<String> verifyConfirmationCode(@Valid @RequestBody CodeVerificationRequest request) {
        userActivityService.verifyCode(request);
        return ResponseEntity.ok("Code verified");
    }
}