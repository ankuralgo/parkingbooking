package org.ankur.parkinglot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class EntryTicketDTO {

    private long ticketNo;
    private VehicleType vehicleType;
    private int slotNumber;
    private Instant entryTime;


}
