package com.ali.bugtracker.entities;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bug extends Auditable<String>{
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
    private String description;

    @NotEmpty(message = "choose Bug severity")
    @Pattern(regexp = "^(LOW|AVERAGE|CRITICAL)$" ,message = "invalid severity")
    private String severity; // low , average, critical

    private boolean fixed;

    public Bug(Employee employeeId, Ticket ticketId, String description,String severity, boolean fixed){
        this.employeeId=employeeId;
        this.ticketId=ticketId;
        this.description=description;
        this.severity=severity;
        this.fixed=fixed;

    }
}
