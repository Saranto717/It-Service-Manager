package com.andreas.itservicemanager.controller;

import com.andreas.itservicemanager.dto.UserResponse;
import com.andreas.itservicemanager.entity.Role;
import com.andreas.itservicemanager.service.JwtService;
import com.andreas.itservicemanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.andreas.itservicemanager.config.SecurityConfig;
import com.andreas.itservicemanager.security.JwtAuthenticationFilter;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;



    @Test
    void getAllUsers_shouldReturn200_whenUserIsAdmin() throws Exception {

        when(userService.getAllUsers())
                .thenReturn(java.util.List.of(
                        new UserResponse(
                                1L,
                                "System",
                                "Admin",
                                "admin@test.com",
                                Role.ADMIN
                        )
                ));

        mockMvc.perform(
                        get("/api/users")
                                .with(user("admin@test.com").roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("admin@test.com"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }

    @Test
    void getUserById_shouldReturn200_whenUserIsAdmin() throws Exception {

        UserResponse response = new UserResponse(
                1L,
                "John",
                "Doe",
                "john@test.com",
                Role.USER
        );

        when(userService.getUserById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/users/1")
                                .with(user("admin@test.com").roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void deleteUser_shouldReturn204_whenUserIsAdmin() throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/users/1")
                                .with(user("admin@test.com").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        org.mockito.Mockito.verify(userService)
                .deleteUser(1L);
    }

    @Test
    void updateUser_shouldReturn200_whenUserIsAdmin() throws Exception {

        UserResponse response = new UserResponse(
                1L,
                "Updated",
                "User",
                "updated@test.com",
                Role.USER
        );

        when(userService.updateUser(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(response);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/users/1")
                                .with(user("admin@test.com").roles("ADMIN"))
                                .with(csrf())
                                .contentType("application/json")
                                .content("""
                                {
                                    "firstName": "Updated",
                                    "lastName": "User",
                                    "email": "updated@test.com",
                                    "role": "USER"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("updated@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }


    @Test
    void createUser_shouldReturn201_whenUserIsAdmin() throws Exception {

        UserResponse response = new UserResponse(
                2L,
                "New",
                "User",
                "new@test.com",
                Role.USER
        );

        when(userService.createUser(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any(Role.class)
        )).thenReturn(response);
        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .post("/api/users")
                                .with(user("admin@test.com").roles("ADMIN"))
                                .with(csrf())
                                .contentType("application/json")
                                .content("""
                                {
                                    "firstName": "New",
                                    "lastName": "User",
                                    "email": "new@test.com",
                                    "password": "Password123!",
                                    "role": "USER"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("new@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
    @Test
    void createUser_shouldReturn400_whenEmailIsInvalid() throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .post("/api/users")
                                .with(user("admin@test.com").roles("ADMIN"))
                                .with(csrf())
                                .contentType("application/json")
                                .content("""
                                {
                                    "firstName": "New",
                                    "lastName": "User",
                                    "email": "invalid-email",
                                    "password": "Password123!",
                                    "role": "USER"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());
    }
}