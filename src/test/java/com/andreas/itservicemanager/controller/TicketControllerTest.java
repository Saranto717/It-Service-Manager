package com.andreas.itservicemanager.controller;

import com.andreas.itservicemanager.dto.CreateTicketRequest;
import com.andreas.itservicemanager.dto.TicketResponse;
import com.andreas.itservicemanager.entity.TicketPriority;
import com.andreas.itservicemanager.entity.TicketStatus;
import com.andreas.itservicemanager.service.JwtService;
import com.andreas.itservicemanager.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void createTicket_shouldReturn201_whenRequestIsValid() throws Exception {

        TicketResponse response = new TicketResponse(
                1L,
                "Computer not working",
                "The computer does not turn on",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                LocalDateTime.now(),
                1L,
                null
        );

        when(ticketService.createTicket(any(CreateTicketRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/tickets")
                                .with(user("user@test.com").roles("USER"))
                                .with(csrf())
                                .contentType("application/json")
                                .content("""
                                    {
                                        "title": "Computer not working",
                                        "description": "The computer does not turn on",
                                        "priority": "HIGH",
                                        "createdById": 1
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Computer not working"))
                .andExpect(jsonPath("$.description").value("The computer does not turn on"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.createdById").value(1));
    }
    @Test
    void getAllTickets_shouldReturn200_whenUserIsAuthenticated() throws Exception {

        when(ticketService.getAllTickets())
                .thenReturn(java.util.List.of(
                        new TicketResponse(
                                1L,
                                "Computer not working",
                                "The computer does not turn on",
                                TicketStatus.OPEN,
                                TicketPriority.HIGH,
                                LocalDateTime.now(),
                                1L,
                                null
                        )
                ));

        mockMvc.perform(
                        get("/api/tickets")
                                .with(user("user@test.com").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Computer not working"))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }
    @Test
    void getTicketById_shouldReturn200_whenUserIsAuthenticated() throws Exception {

        TicketResponse response = new TicketResponse(
                1L,
                "Computer not working",
                "The computer does not turn on",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                LocalDateTime.now(),
                1L,
                null
        );

        when(ticketService.getTicketById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/tickets/1")
                                .with(user("user@test.com").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Computer not working"))
                .andExpect(jsonPath("$.description").value("The computer does not turn on"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.createdById").value(1));
    }
    @Test
    void assignTicket_shouldReturn200_whenUserIsTechnician() throws Exception {

        TicketResponse response = new TicketResponse(
                1L,
                "Computer not working",
                "The computer does not turn on",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                LocalDateTime.now(),
                1L,
                2L
        );

        when(ticketService.assignTicket(
                org.mockito.ArgumentMatchers.eq(1L),
                any(com.andreas.itservicemanager.dto.AssignTicketRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/tickets/1/assign")
                                .with(user("tech@test.com").roles("TECHNICIAN"))
                                .with(csrf())
                                .contentType("application/json")
                                .content("""
                                {
                                    "technicianId": 2
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.assignedToId").value(2));
    }
    @Test
    void updateTicketStatus_shouldReturn200_whenUserIsTechnician() throws Exception {

        TicketResponse response = new TicketResponse(
                1L,
                "Computer not working",
                "The computer does not turn on",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                LocalDateTime.now(),
                1L,
                2L
        );

        when(ticketService.updateTicketStatus(
                org.mockito.ArgumentMatchers.eq(1L),
                any(com.andreas.itservicemanager.dto.UpdateTicketStatusRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/tickets/1/status")
                                .with(user("tech@test.com").roles("TECHNICIAN"))
                                .with(csrf())
                                .contentType("application/json")
                                .content("""
                                {
                                    "status": "RESOLVED"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("RESOLVED"))
                .andExpect(jsonPath("$.assignedToId").value(2));
    }
    @Test
    void createTicket_shouldReturn400_whenTitleIsBlank() throws Exception {

        mockMvc.perform(
                        post("/api/tickets")
                                .with(user("user@test.com").roles("USER"))
                                .with(csrf())
                                .contentType("application/json")
                                .content("""
                                {
                                    "title": "",
                                    "description": "The computer does not turn on",
                                    "priority": "HIGH",
                                    "createdById": 1
                                }
                                """)
                )
                .andExpect(status().isBadRequest());
    }
}