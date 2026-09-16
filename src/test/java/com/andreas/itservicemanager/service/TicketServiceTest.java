package com.andreas.itservicemanager.service;

import com.andreas.itservicemanager.dto.AssignTicketRequest;
import com.andreas.itservicemanager.dto.CreateTicketRequest;
import com.andreas.itservicemanager.dto.TicketResponse;
import com.andreas.itservicemanager.dto.UpdateTicketStatusRequest;
import com.andreas.itservicemanager.entity.Role;
import com.andreas.itservicemanager.entity.Ticket;
import com.andreas.itservicemanager.entity.TicketPriority;
import com.andreas.itservicemanager.entity.TicketStatus;
import com.andreas.itservicemanager.entity.User;
import com.andreas.itservicemanager.exception.ResourceNotFoundException;
import com.andreas.itservicemanager.repository.TicketRepository;
import com.andreas.itservicemanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void createTicket_shouldCreateOpenTicket() {


        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("Printer not working");
        request.setDescription("The printer does not print.");
        request.setPriority(TicketPriority.HIGH);
        request.setCreatedById(1L);

        User creator = new User(
                "John",
                "Doe",
                "john@test.com",
                "password",
                Role.USER
        );

        creator.setId(1L);


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(creator));

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TicketResponse response = ticketService.createTicket(request);

        assertNotNull(response);
        assertEquals("Printer not working", response.getTitle());
        assertEquals(TicketStatus.OPEN, response.getStatus());
        assertEquals(TicketPriority.HIGH, response.getPriority());
        assertEquals(1L, response.getCreatedById());
        assertNull(response.getAssignedToId());

        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void createTicket_shouldThrowException_whenUserDoesNotExist() {

        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("Printer not working");
        request.setDescription("The printer does not print.");
        request.setPriority(TicketPriority.HIGH);
        request.setCreatedById(999L);

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.createTicket(request)
        );

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }
    @Test
    void assignTicket_shouldAssignTechnicianAndSetInProgress() {

        User creator = new User(
                "John",
                "Doe",
                "john@test.com",
                "hashedPassword",
                Role.USER
        );

        User technician = new User(
                "Mike",
                "Smith",
                "mike@test.com",
                "hashedPassword",
                Role.TECHNICIAN
        );

        technician.setId(2L);

        Ticket ticket = new Ticket();
        ticket.setTitle("Printer not working");
        ticket.setDescription("The printer does not print.");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setCreatedAt(java.time.LocalDateTime.now());
        ticket.setCreatedBy(creator);

        AssignTicketRequest request = new AssignTicketRequest();
        request.setTechnicianId(2L);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(technician));

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TicketResponse response =
                ticketService.assignTicket(1L, request);

        assertNotNull(response);
        assertEquals(TicketStatus.IN_PROGRESS, response.getStatus());
        assertEquals(2L, response.getAssignedToId());

        verify(ticketRepository).save(ticket);
    }
    @Test
    void assignTicket_shouldThrowException_whenUserIsNotTechnician() {

        User creator = new User(
                "John",
                "Doe",
                "john@test.com",
                "hashedPassword",
                Role.USER
        );

        User user = new User(
                "Mike",
                "Smith",
                "mike@test.com",
                "hashedPassword",
                Role.USER
        );

        Ticket ticket = new Ticket();
        ticket.setTitle("Printer not working");
        ticket.setDescription("The printer does not print.");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setCreatedAt(java.time.LocalDateTime.now());
        ticket.setCreatedBy(creator);

        AssignTicketRequest request = new AssignTicketRequest();
        request.setTechnicianId(2L);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        assertThrows(
                RuntimeException.class,
                () -> ticketService.assignTicket(1L, request)
        );

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }
    @Test
    void updateTicketStatus_shouldUpdateStatus() {

        User creator = new User(
                "John",
                "Doe",
                "john@test.com",
                "hashedPassword",
                Role.USER
        );

        creator.setId(1L);

        Ticket ticket = new Ticket();
        ticket.setTitle("Printer not working");
        ticket.setDescription("The printer does not print.");
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setCreatedAt(java.time.LocalDateTime.now());
        ticket.setCreatedBy(creator);

        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest();
        request.setStatus(TicketStatus.RESOLVED);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TicketResponse response =
                ticketService.updateTicketStatus(1L, request);

        assertNotNull(response);
        assertEquals(TicketStatus.RESOLVED, response.getStatus());

        verify(ticketRepository).save(ticket);
    }
    @Test
    void updateTicketStatus_shouldThrowException_whenTicketDoesNotExist() {

        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest();
        request.setStatus(TicketStatus.RESOLVED);

        when(ticketRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicketStatus(999L, request)
        );

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }
}