package com.petitionvoice.backend.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address", example = "mos@craciun.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "I hope you remember it", example = "carbune")
    private String password;
}