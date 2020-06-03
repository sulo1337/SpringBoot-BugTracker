package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class Comment extends Auditable <Comment> {
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
    private String commentText;
 //   private String creationDate;

    public Comment(Employee employeeId, Ticket ticketId, String commentText) {
        this.employeeId = employeeId;
        this.ticketId = ticketId;
        this.commentText = commentText;
     //   this.creationDate = creationDate;
    }
}
