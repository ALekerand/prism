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
    private final List<String> roles;
    private final Integer idRegion;
    private final Integer idDrena;
    private final Integer idIep;
    private final Integer idDepartement;
    private final Integer idSousPrefecture;
    private final Integer idCommune;
    private final Integer idLocalite;

    public AuthUser(Integer userId, String username, String passwordHash, boolean enabled, List<String> permissions) {
        this(userId, username, passwordHash, enabled, permissions, List.of(), null, null, null, null, null, null, null);
    }

    public AuthUser(
            Integer userId,
            String username,
            String passwordHash,
            boolean enabled,
            List<String> permissions,
            List<String> roles,
            Integer idRegion,
            Integer idDrena,
            Integer idIep,
            Integer idDepartement,
            Integer idSousPrefecture,
            Integer idCommune,
            Integer idLocalite) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.permissions = permissions != null ? List.copyOf(permissions) : List.of();
        this.roles = roles != null ? List.copyOf(roles) : List.of();
        this.idRegion = idRegion;
        this.idDrena = idDrena;
        this.idIep = idIep;
        this.idDepartement = idDepartement;
        this.idSousPrefecture = idSousPrefecture;
        this.idCommune = idCommune;
        this.idLocalite = idLocalite;
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

    public boolean hasAnyRole(String... roleCodes) {
        if (roleCodes == null || roleCodes.length == 0) {
            return false;
        }
        for (String roleCode : roleCodes) {
            if (roles.contains(roleCode)) {
                return true;
            }
        }
        return false;
    }
}
