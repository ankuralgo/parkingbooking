package org.ankur.parkinglot.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ParkingSlot {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "parkingMeta")
    private ParkingMeta parkingMeta;
    private int spotNumber;
    private boolean occupied;
    private String vehicleType;
/*    private String building_name; // TODO: can be used for further use cases
    private int floor;
    private String area_name;*/
}
