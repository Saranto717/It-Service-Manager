package com.andreas.itservicemanager.controller;

import com.andreas.itservicemanager.dto.AssignTicketRequest;
import com.andreas.itservicemanager.dto.CreateTicketRequest;
import com.andreas.itservicemanager.dto.TicketResponse;
import com.andreas.itservicemanager.dto.UpdateTicketStatusRequest;
import com.andreas.itservicemanager.entity.Ticket;
import com.andreas.itservicemanager.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @Operation(
            summary = "Create a new ticket",
            description = "Creates a new IT support ticket."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(
            @Valid @RequestBody CreateTicketRequest request
    ) {
        return ticketService.createTicket(request);
    }




    @Operation(
            summary = "Get all tickets",
            description = "Returns all IT support tickets."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })

   @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }



    @Operation(
            summary = "Get ticket by ID",
            description = "Returns a specific ticket by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })



    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable("id") Long id) {
        return ticketService.getTicketById(id);
    }



    @Operation(
            summary = "Assign ticket to technician",
            description = "Assigns an existing ticket to a technician and changes its status to IN_PROGRESS."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket assigned successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Ticket or technician not found")
    })


    @PutMapping("/{id}/assign")
    public TicketResponse assignTicket(
            @PathVariable("id") Long id,
            @RequestBody AssignTicketRequest request
    ) {
        return ticketService.assignTicket(id, request);
    }


    @Operation(
            summary = "Update ticket status",
            description = "Updates the status of an existing ticket."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket status updated successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })


    @PutMapping("/{id}/status")
    public TicketResponse updateTicketStatus(
            @PathVariable("id") Long id,
            @RequestBody UpdateTicketStatusRequest request
    ) {
        return ticketService.updateTicketStatus(id, request);
    }
}
