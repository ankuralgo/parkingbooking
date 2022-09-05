package org.ankur.parkinglot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ankur.parkinglot.fees.FeeType;

@Data
@AllArgsConstructor
public class FeedModelDTO {

    private int startingHour;
    private int endingHour;
    private int fee;
    private int order;
    private FeeType feeType;
    private VehicleType vehicleType;
}
