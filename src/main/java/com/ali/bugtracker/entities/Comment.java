package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class Comment extends Auditable <String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employeeId;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticketId;
    @NotEmpty(message = "write your comment first")
    @Size(min = 1,max = 99)
    private String commentText;

    public Comment(Employee employeeId, Ticket ticketId, String commentText) {
        this.employeeId = employeeId;
        this.ticketId = ticketId;
        this.commentText = commentText;
    }
}
