package com.dcspa.prism.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.dcspa.prism.dto.LoginRequest;
import com.dcspa.prism.dto.LoginResponse;
import com.dcspa.prism.entity.AppRole;
import com.dcspa.prism.entity.AppUser;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.security.JwtUtil;
import java.util.Set;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceLoginTest {

	@Mock
	private AppUserRepository appUserRepository;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthService authService;

	@Test
	void loginReturnsTokenWhenPasswordMatches() {
		AppRole role = new AppRole();
		role.setCodeRole("ADMIN");
		AppUser user = new AppUser();
		user.setId(1);
		user.setUsername("admin");
		user.setPasswordHash("{bcrypt}enc");
		user.setEmail("a@x");
		user.setActif(true);
		user.setRoles(Set.of(role));

		when(appUserRepository.findByUsername("admin")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("secret", "{bcrypt}enc")).thenReturn(true);
		when(jwtUtil.generateToken("admin")).thenReturn("jwt-token");

		LoginRequest req = new LoginRequest();
		req.setUsername("admin");
		req.setPassword("secret");
		LoginResponse res = authService.login(req);
		assertEquals("jwt-token", res.getToken());
		assertEquals("admin", res.getUsername());
		assertEquals(1, res.getUserId());
	}

	@Test
	void loginThrowsWhenPasswordWrong() {
		AppUser user = new AppUser();
		user.setUsername("u");
		user.setPasswordHash("h");
		user.setActif(true);
		user.setRoles(Set.of());
		when(appUserRepository.findByUsername("u")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
		LoginRequest bad = new LoginRequest();
		bad.setUsername("u");
		bad.setPassword("bad");
		assertThrows(IllegalArgumentException.class, () -> authService.login(bad));
	}
}
