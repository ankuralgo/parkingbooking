package org.ankur.parkinglot.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Receipt {
    @Id
    @GeneratedValue
    private long id;
    private int spotNumber;
    private float fee;
}
