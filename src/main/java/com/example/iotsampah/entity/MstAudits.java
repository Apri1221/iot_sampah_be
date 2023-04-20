package com.example.iotsampah.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "mst_audits")
public class MstAudits {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(targetEntity=MstUsers.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private MstUsers createdBy;

    private int point;
    private int saldo;

    private String type; // LOG / MUTATION

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", length = 19)
    private Date createdDate;
}
