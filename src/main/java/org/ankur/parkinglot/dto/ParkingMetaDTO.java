package org.ankur.parkinglot.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ParkingMetaDTO {
    private long parkingId;
    private String city;
    private String area;
    private ParkingType parkingType;
    private Map<VehicleType, Integer> vehicleSlots;
    private List<FeedModelDTO> feeModel;
}
