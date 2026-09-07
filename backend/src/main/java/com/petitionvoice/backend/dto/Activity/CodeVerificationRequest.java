package com.petitionvoice.backend.dto.Activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeVerificationRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String code;
}