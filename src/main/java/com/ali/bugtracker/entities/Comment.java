package com.ali.bugtracker.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Comment extends Auditable <String> {
    public Comment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
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

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

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
