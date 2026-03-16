package com.dcspa.prism.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class AuthUser implements UserDetails {

    private final Integer userId;
    private final String username;
    private final String passwordHash;
    private final boolean enabled;
    private final List<String> permissions;

    public AuthUser(Integer userId, String username, String passwordHash, boolean enabled, List<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.permissions = permissions != null ? List.copyOf(permissions) : List.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = permissions.stream()
                .map(p -> new SimpleGrantedAuthority("PERMISSION_" + p))
                .collect(Collectors.toList());
        return Stream.concat(
                authorities.stream(),
                Stream.of(new SimpleGrantedAuthority("ROLE_AUTHENTICATED"))
        ).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }

    public boolean hasPermission(String fonctionnaliteCode, String permissionCode) {
        String key = fonctionnaliteCode + ":" + permissionCode;
        return permissions.contains(key);
    }
}
