package org.ankur.parkinglot.repository;

import org.ankur.parkinglot.domain.ParkingMeta;
import org.ankur.parkinglot.domain.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {


    ParkingSlot findFirstByVehicleTypeAndOccupiedAndParkingMetaOrderById(String vType, boolean isOccupied, ParkingMeta parkingMeta);
}
