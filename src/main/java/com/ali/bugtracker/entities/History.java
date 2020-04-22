package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "history_id_seq")
    private long id;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employeeId;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticketId;

    private String oldStatus;
    private LocalDateTime modificationDate;

    public History(Employee employeeId, Ticket ticketId, String oldStatus, LocalDateTime modificationDate) {
        this.employeeId = employeeId;
        this.ticketId = ticketId;
        this.oldStatus = oldStatus;
        this.modificationDate = modificationDate;
    }
}
