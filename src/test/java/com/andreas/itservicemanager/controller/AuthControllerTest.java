package com.andreas.itservicemanager.controller;

import com.andreas.itservicemanager.dto.LoginResponse;
import com.andreas.itservicemanager.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.andreas.itservicemanager.service.JwtService;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.security.test.context.support.WithMockUser;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private JwtService jwtService;
    @WithMockUser
    @Test
    void login_shouldReturn200_whenCredentialsAreValid() throws Exception {

        LoginResponse response = new LoginResponse(
                "fake-jwt-token",
                "admin@test.com",
                "ADMIN"
        );

        when(authService.login(any()))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                        {
                            "email": "admin@test.com",
                            "password": "Admin123!"
                        }
                        """)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}