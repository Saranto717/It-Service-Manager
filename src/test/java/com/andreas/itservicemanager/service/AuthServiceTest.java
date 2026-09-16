package com.andreas.itservicemanager.service;

import com.andreas.itservicemanager.dto.LoginRequest;
import com.andreas.itservicemanager.dto.LoginResponse;
import com.andreas.itservicemanager.entity.Role;
import com.andreas.itservicemanager.entity.User;
import com.andreas.itservicemanager.exception.InvalidCredentialsException;
import com.andreas.itservicemanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnToken_whenCredentialsAreCorrect() {

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@test.com");
        request.setPassword("Admin123!");

        User user = new User(
                "System",
                "Admin",
                "admin@test.com",
                "hashedPassword",
                Role.ADMIN
        );

        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Admin123!",
                "hashedPassword"
        )).thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("fake-jwt-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("admin@test.com", response.getEmail());
        assertEquals("ADMIN", response.getRole());

        verify(jwtService).generateToken(user);
    }
    @Test
    void login_shouldThrowException_whenEmailDoesNotExist() {

        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@test.com");
        request.setPassword("Admin123!");

        when(userRepository.findByEmail("unknown@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(any());
    }
    @Test
    void login_shouldThrowException_whenPasswordIsIncorrect() {

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@test.com");
        request.setPassword("WrongPassword");

        User user = new User(
                "System",
                "Admin",
                "admin@test.com",
                "hashedPassword",
                Role.ADMIN
        );

        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "WrongPassword",
                "hashedPassword"
        )).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never())
                .generateToken(any());
    }
}