package com.andreas.itservicemanager.service;

import com.andreas.itservicemanager.dto.LoginRequest;
import com.andreas.itservicemanager.dto.LoginResponse;
import com.andreas.itservicemanager.entity.User;
import com.andreas.itservicemanager.exception.InvalidCredentialsException;
import com.andreas.itservicemanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }
        String token = jwtService.generateToken(user);
        return new LoginResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}