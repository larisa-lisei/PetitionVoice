package com.petitionvoice.backend.dto.Petition;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestSignatureRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address", example = "string@gmail.com")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Schema(description = "Phone number", example = "078250044")
    private String telephone;
}