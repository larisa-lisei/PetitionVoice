package com.petitionvoice.backend.controller;

import com.petitionvoice.backend.dto.Petition.*;
import com.petitionvoice.backend.services.PetitionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/petitions")
@RestController
@SecurityRequirement(name = "authorization")
public class PetitionController {

    private final PetitionService petitionService;

    public PetitionController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    @GetMapping
    public ResponseEntity<Page<PetitionResponse>> getAllApprovedPetitions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String orderBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(petitionService.getAllApprovedPetitions(keyword, categories, orderBy, page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PetitionResponse>> getAllPetitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(petitionService.getAllPetitions(page, size));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<PetitionResponse> getPetitionDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(petitionService.getPetitionById(id));
    }

    @GetMapping("/{id}/signatures")
    public ResponseEntity<List<String>> getPetitionSignatures(@PathVariable Integer id) {
        return ResponseEntity.ok(petitionService.getSignatures(id));
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<String> getPetitionStatistics(@PathVariable Integer id) {
        return ResponseEntity.ok(petitionService.getStatistics(id));
    }

    @GetMapping("/petition-of-the-day")
    public ResponseEntity<PetitionResponse> getPetitionOfTheDay() {
        return ResponseEntity.ok(petitionService.getPetitionOfTheDay());
    }

    @PostMapping
    public ResponseEntity<PetitionResponse> createPetition(
            @Valid @RequestBody PetitionCreateRequest payload
    ) {
        PetitionResponse response = petitionService.createPetition(payload);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/signatures")
    public ResponseEntity<String> signPetition(
            @PathVariable Integer id,
            @Valid @RequestBody GuestSignatureRequest payload
    ) {
        petitionService.signPetition(id, payload);
        return ResponseEntity.ok("Signature added successfully");
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<String> giveFeedback(
            @PathVariable Integer id,
            @Valid @RequestBody PetitionFeedbackRequest payload
    ) {
        petitionService.giveFeedback(id, payload);
        return ResponseEntity.ok("Feedback submitted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetitionResponse> updatePetition(
            @PathVariable Integer id,
            @Valid @RequestBody PetitionUpdateRequest payload
    ) {
        PetitionResponse response = petitionService.updatePetition(id, payload);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePetition(@PathVariable Integer id) {
        petitionService.deletePetition(id);
        return ResponseEntity.ok("Petition deleted successfully");
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file
    ) {
        petitionService.uploadImage(id, file);
        return ResponseEntity.ok("Image uploaded successfully");
    }
}