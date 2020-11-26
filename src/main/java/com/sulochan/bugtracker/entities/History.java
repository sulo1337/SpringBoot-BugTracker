package com.sulochan.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

public class History {
    public History() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long historyId;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employeeId;

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Ticket getTicketId() {
        return ticketId;
    }

    public void setTicketId(Ticket ticketId) {
        this.ticketId = ticketId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticketId;
    private String event;
    private String modificationDate;

    public History(Employee employeeId, Ticket ticketId, String event, String modificationDate) {
        this.employeeId = employeeId;
        this.ticketId = ticketId;
        this.event = event;
        this.modificationDate = modificationDate;
    }
}
