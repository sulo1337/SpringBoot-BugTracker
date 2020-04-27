package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "comment_id_seq")
    private long commentId;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employeeId;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticketId;

    private String commentText;
    private String creationDate;

    public Comment(Employee employeeId, Ticket ticketId, String commentText, String creationDate) {
        this.employeeId = employeeId;
        this.ticketId = ticketId;
        this.commentText = commentText;
        this.creationDate = creationDate;
    }
}
