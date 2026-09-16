package com.andreas.itservicemanager.dto;

import com.andreas.itservicemanager.entity.TicketPriority;
import com.andreas.itservicemanager.entity.TicketStatus;

import java.time.LocalDateTime;

public class TicketResponse {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private LocalDateTime createdAt;
    private Long createdById;
    private Long assignedToId;

    public TicketResponse(
            Long id,
            String title,
            String description,
            TicketStatus status,
            TicketPriority priority,
            LocalDateTime createdAt,
            Long createdById,
            Long assignedToId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.createdById = createdById;
        this.assignedToId = assignedToId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }
}