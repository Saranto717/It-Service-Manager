package com.andreas.itservicemanager.service;
import com.andreas.itservicemanager.dto.AssignTicketRequest;
import com.andreas.itservicemanager.dto.CreateTicketRequest;
import com.andreas.itservicemanager.dto.TicketResponse;
import com.andreas.itservicemanager.dto.UpdateTicketStatusRequest;
import com.andreas.itservicemanager.entity.Ticket;
import com.andreas.itservicemanager.entity.TicketStatus;
import com.andreas.itservicemanager.entity.User;
import com.andreas.itservicemanager.exception.ResourceNotFoundException;
import com.andreas.itservicemanager.repository.TicketRepository;
import com.andreas.itservicemanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;


    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public TicketResponse createTicket(CreateTicketRequest request) {

        User user = userRepository.findById(request.getCreatedById())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.getCreatedById()
                        )
                );

        Ticket ticket = new Ticket();

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setCreatedBy(user);
        Ticket savedTicket = ticketRepository.save(ticket);

        return new TicketResponse(
                savedTicket.getId(),
                savedTicket.getTitle(),
                savedTicket.getDescription(),
                savedTicket.getStatus(),
                savedTicket.getPriority(),
                savedTicket.getCreatedAt(),
                savedTicket.getCreatedBy().getId(),
                savedTicket.getAssignedTo() != null
                        ? savedTicket.getAssignedTo().getId()
                        : null
        );
    }
    private TicketResponse mapToTicketResponse(Ticket ticket) {

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedAt(),
                ticket.getCreatedBy().getId(),
                ticket.getAssignedTo() != null
                        ? ticket.getAssignedTo().getId()
                        : null
        );
    }
    public List<TicketResponse> getAllTickets() {

        return ticketRepository.findAll()
                .stream()
                .map(this::mapToTicketResponse)
                .toList();
    }
    public TicketResponse getTicketById(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found with id: " + id
                        )
                );

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedAt(),
                ticket.getCreatedBy().getId(),
                ticket.getAssignedTo() != null
                        ? ticket.getAssignedTo().getId()
                        : null
        );
    }
    public TicketResponse assignTicket(Long ticketId, AssignTicketRequest request) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found with id: " + ticketId
                        )
                );

        User technician = userRepository.findById(request.getTechnicianId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.getTechnicianId()
                        )
                );

        if (technician.getRole() != com.andreas.itservicemanager.entity.Role.TECHNICIAN) {
            throw new RuntimeException("User is not a technician");
        }

        ticket.setAssignedTo(technician);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        Ticket updatedTicket = ticketRepository.save(ticket);

        return new TicketResponse(
                updatedTicket.getId(),
                updatedTicket.getTitle(),
                updatedTicket.getDescription(),
                updatedTicket.getStatus(),
                updatedTicket.getPriority(),
                updatedTicket.getCreatedAt(),
                updatedTicket.getCreatedBy().getId(),
                updatedTicket.getAssignedTo().getId()
        );
    }
    public TicketResponse updateTicketStatus(
            Long ticketId,
            UpdateTicketStatusRequest request
    ) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found with id: " + ticketId
                        )
                );

        ticket.setStatus(request.getStatus());

        Ticket updatedTicket = ticketRepository.save(ticket);

        return new TicketResponse(
                updatedTicket.getId(),
                updatedTicket.getTitle(),
                updatedTicket.getDescription(),
                updatedTicket.getStatus(),
                updatedTicket.getPriority(),
                updatedTicket.getCreatedAt(),
                updatedTicket.getCreatedBy().getId(),
                updatedTicket.getAssignedTo() != null
                        ? updatedTicket.getAssignedTo().getId()
                        : null
        );
    }
}