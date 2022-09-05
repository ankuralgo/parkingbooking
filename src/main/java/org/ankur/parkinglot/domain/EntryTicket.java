package org.ankur.parkinglot.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class EntryTicket {

    @Id
    @GeneratedValue
    private long id;
    private long entryTime;
    private String vehicleType;
    @ManyToOne
    @JoinColumn(name = "slotNo")
    private ParkingSlot slotNo;
}
