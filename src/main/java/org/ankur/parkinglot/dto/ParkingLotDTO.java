package org.ankur.parkinglot.dto;

import lombok.Data;

@Data
public class ParkingLotDTO {

    private int parkingId;
    private int lot;
    private String vehicleType;
    private String building_name;
    private int floor;
    private String area_name;

}
