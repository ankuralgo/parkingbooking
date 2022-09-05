package org.ankur.parkinglot.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class ParkingMeta {

    @Id
    @GeneratedValue
    private long id;
    private String city;
    private String area;
    private String parkingType;
}
