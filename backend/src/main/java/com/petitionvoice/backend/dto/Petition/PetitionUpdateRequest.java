package com.petitionvoice.backend.dto.Petition;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PetitionUpdateRequest {

    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    @Min(value = 1, message = "Goal must be at least 1 signature")
    private Integer goal;

    @Future(message = "Expiration date must be in the future")
    private Date expirationDate;
}