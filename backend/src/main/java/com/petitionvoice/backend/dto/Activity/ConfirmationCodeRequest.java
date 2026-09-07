package com.petitionvoice.backend.dto.Activity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmationCodeRequest {
    @NotBlank
    @Email
    private String email;
}