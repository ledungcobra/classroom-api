package com.ledungcobra.configuration.security.userdetails;

import com.ledungcobra.common.EUserStatus;
import com.ledungcobra.user.entity.User;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails, Serializable {

    private final User user;

    public AppUserDetails(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.getRole());
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user != null &&
                user.getUserStatus() == EUserStatus.Active.getValue();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user != null && user.getEmailConfirmed() == 1;
    }

    public User unwrap() {
        return user;
    }

}
