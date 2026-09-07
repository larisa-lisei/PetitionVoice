package com.petitionvoice.backend.dto.Petition;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PetitionCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Goal is required")
    @Min(value = 1, message = "Goal must be at least 1 signature")
    private Integer goal;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private Date expirationDate;
}