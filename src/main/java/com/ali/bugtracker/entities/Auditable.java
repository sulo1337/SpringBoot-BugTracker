package com.ali.bugtracker.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class Auditable<U> {

    @Column(updatable = false)
    @CreatedDate
    @Temporal(TemporalType.DATE)
    protected Date createdDate;

    @LastModifiedBy
    protected String lastModifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    protected Date lastModifiedDate;


}
