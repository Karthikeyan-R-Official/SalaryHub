
package com.salaryhub.service;

import com.salaryhub.dto.LoginRequest;
import com.salaryhub.dto.LoginResponse;
import com.salaryhub.entity.AppUser;
import com.salaryhub.entity.Role;
import com.salaryhub.repository.AppUserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldReturnTokenForValidCredentials() {

        LoginRequest request =
                new LoginRequest(" hrmanager ", "correctPassword");

        AppUser user = mock(AppUser.class);

        when(appUserRepository.findByUsername("hrmanager"))
                .thenReturn(Optional.of(user));

        when(user.isEnabled()).thenReturn(true);
        when(passwordEncoder.matches(
                "correctPassword", "hashedPassword"))
                .thenReturn(true);

        when(user.getPasswordHash()).thenReturn("hashedPassword");
        when(user.getUsername()).thenReturn("hrmanager");
        when(user.getRole()).thenReturn(Role.HR_MANAGER);

        when(jwtService.generateToken(user))
                .thenReturn("sample-jwt-token");

        when(jwtService.getExpirationSeconds())
                .thenReturn(3600L);

        LoginResponse response = authService.login(request);

        assertEquals("sample-jwt-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(3600L, response.expiresIn());
        assertEquals("hrmanager", response.username());
        assertEquals("HR_MANAGER", response.role());

        verify(jwtService).generateToken(user);
    }

    @Test
    void loginShouldRejectUnknownUsername() {

        LoginRequest request =
                new LoginRequest("unknown", "password");

        when(appUserRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void loginShouldRejectIncorrectPassword() {

        LoginRequest request =
                new LoginRequest("hrmanager", "wrongPassword");

        AppUser user = mock(AppUser.class);

        when(appUserRepository.findByUsername("hrmanager"))
                .thenReturn(Optional.of(user));

        when(user.isEnabled()).thenReturn(true);
        when(user.getPasswordHash()).thenReturn("hashedPassword");

        when(passwordEncoder.matches(
                "wrongPassword", "hashedPassword"))
                .thenReturn(false);

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void loginShouldRejectDisabledAccount() {

        LoginRequest request =
                new LoginRequest("hrmanager", "correctPassword");

        AppUser user = mock(AppUser.class);

        when(appUserRepository.findByUsername("hrmanager"))
                .thenReturn(Optional.of(user));

        when(user.isEnabled()).thenReturn(false);

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never()).generateToken(any());
    }
}
