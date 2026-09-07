package com.petitionvoice.backend.services;

import com.petitionvoice.backend.model.User;
import com.petitionvoice.backend.repository.UserRepository;
import com.petitionvoice.backend.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        User user;

        if (input.contains("@")) {
            user = userRepository.findByUserDetails_Email(input)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + input));
        }
        else {
            try {
                Integer id = Integer.parseInt(input);
                user = userRepository.findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
            } catch (NumberFormatException e) {
                throw new UsernameNotFoundException("Invalid identifier: " + input);
            }
        }

        // Lazy load check
        if (user.getUserDetails() != null) {
            user.getUserDetails().getEmail();
            user.getUserDetails().getPassword_hash();
        }

        return new SecurityUser(user);
    }
}