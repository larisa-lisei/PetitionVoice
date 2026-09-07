package com.petitionvoice.backend.security;

import com.petitionvoice.backend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// un Adaptor de care avem nevoie fiindca User-ul e impartit in 2 tabele si avem nevoie de email in loc de username
public class SecurityUser implements UserDetails {
    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getUserDetails().getEmail();
    }

    @Override
    public String getPassword() {
        return user.getUserDetails().getPassword_hash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = (user.getRole() != null) ? user.getRole().name() : "ANONYMOUS";
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public User getUserEntity() { return user; }
}