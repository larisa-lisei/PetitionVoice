package com.petitionvoice.backend.dto.User;

import com.petitionvoice.backend.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private Role role;
    private UserDetailsResponse userDetails;
}