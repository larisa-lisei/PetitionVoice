package com.petitionvoice.backend.mapper;

import com.petitionvoice.backend.dto.User.UserDetailsResponse;
import com.petitionvoice.backend.dto.User.UserResponse;
import com.petitionvoice.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        UserDetailsResponse userDetailsDTO = new UserDetailsResponse();

        if (user.getUserDetails() != null) {
            userDetailsDTO.setId(user.getUserDetails().getId());
            userDetailsDTO.setEmail(user.getUserDetails().getEmail());
            userDetailsDTO.setTelephone(user.getUserDetails().getTelephone());
        }

        UserResponse userDTO = new UserResponse();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirst_name());
        userDTO.setLastName(user.getLast_name());
        userDTO.setRole(user.getRole());
        userDTO.setUserDetails(userDetailsDTO);

        return userDTO;
    }
}