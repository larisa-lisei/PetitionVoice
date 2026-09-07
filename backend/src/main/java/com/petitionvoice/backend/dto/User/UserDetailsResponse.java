package com.petitionvoice.backend.dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {
    private Integer id;
    private String email;
    private String telephone;
}