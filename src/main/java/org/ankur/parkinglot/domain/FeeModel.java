package org.ankur.parkinglot.domain;

import lombok.Data;
import org.ankur.parkinglot.fees.FeeType;

import javax.persistence.*;

@Data
@Entity
public class FeeModel {

    @Id
    @GeneratedValue
    private long id;
    private int startingHour;
    private int endingHour;
    private int fee;
    private int ruleOrder;
    private String vehicleType;
    private FeeType mode;
    @ManyToOne
    @JoinColumn(name = "parkingMeta")
    private ParkingMeta parkingMeta;


}
