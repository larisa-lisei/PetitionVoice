package com.petitionvoice.backend.dto.Petition;

import com.petitionvoice.backend.enums.PetitionState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetitionFeedbackRequest {

    @NotNull(message = "State is required")
    private PetitionState state;

    @Size(min = 5, message = "Feedback must be explicit (at least 5 characters)")
    private String feedback;
}