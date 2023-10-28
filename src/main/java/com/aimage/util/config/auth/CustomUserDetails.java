package com.aimage.util.config.auth;

import com.aimage.domain.user.entity.User;
import com.aimage.util.config.auth.dto.OAuth2Attributes;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Builder
public class CustomUserDetails implements UserDetails, OAuth2User {

    private User user;

    private OAuth2Attributes attributes;

    public static CustomUserDetails of(@NotNull User user, @Nullable OAuth2Attributes attributes) {
        if (attributes == null) {
            return ofFormLogin(user);
        }

        return ofOAuth2Login(user, attributes);
    }

    private static CustomUserDetails ofFormLogin(User user) {
        return CustomUserDetails.builder()
                .user(user)
                .build();
    }

    private static CustomUserDetails ofOAuth2Login(User user, OAuth2Attributes attributes) {
        return CustomUserDetails.builder()
                .user(user)
                .attributes(attributes)
                .build();
    }


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
