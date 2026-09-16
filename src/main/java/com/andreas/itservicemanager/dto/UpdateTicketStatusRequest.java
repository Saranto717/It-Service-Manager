package com.andreas.itservicemanager.dto;

import com.andreas.itservicemanager.entity.TicketStatus;

public class UpdateTicketStatusRequest {

    private TicketStatus status;

    public UpdateTicketStatusRequest() {}

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}