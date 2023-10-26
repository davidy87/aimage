package com.aimage.util.config.auth;

import com.aimage.domain.user.entity.User;
import com.aimage.util.config.auth.dto.OAuth2Attributes;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@RequiredArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final User user;

    private OAuth2Attributes attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes.getAttributes();
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getProvider() {
        return user.getProvider();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
