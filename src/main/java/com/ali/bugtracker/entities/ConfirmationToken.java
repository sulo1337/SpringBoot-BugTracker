package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name = "confirmation_token")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;

    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = Employee.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "employee_id")
    private Employee employee;

    public ConfirmationToken(Employee employee) {
        this.employee = employee;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }
}
