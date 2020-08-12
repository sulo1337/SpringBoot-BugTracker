package com.ali.bugtracker.entities;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
}
