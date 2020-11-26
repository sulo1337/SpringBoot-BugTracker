package com.ali.bugtracker.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Bug extends Auditable<String>{
    public Bug() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bugId;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employeeId;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticketId;

    @NotEmpty(message = "write your description")
    @Size(min = 1,max = 495)
    private String description;

    @NotEmpty(message = "choose Bug severity")
    @Pattern(regexp = "^(LOW|AVERAGE|CRITICAL)$" ,message = "invalid severity")
    private String severity; // low , average, critical

    private boolean fixed;
    @Size(max =249)
    private String imagePath;

    public Bug(Employee employeeId, Ticket ticketId, String description,String severity, boolean fixed,String imagePath){
        this.employeeId=employeeId;
        this.ticketId=ticketId;
        this.description=description;
        this.severity=severity;
        this.fixed=fixed;
        this.imagePath=imagePath;
    }

    public long getBugId() {
        return bugId;
    }

    public void setBugId(long bugId) {
        this.bugId = bugId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
