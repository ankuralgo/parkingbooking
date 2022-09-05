package org.ankur.parkinglot.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
public class Invoice {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "ticket")
    private EntryTicket ticket;
    private Instant exitTime;
    private float fees;

}
