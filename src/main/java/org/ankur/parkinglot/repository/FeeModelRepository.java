package org.ankur.parkinglot.repository;

import org.ankur.parkinglot.domain.FeeModel;
import org.ankur.parkinglot.domain.ParkingMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeModelRepository extends JpaRepository<FeeModel, Long> {


    List<FeeModel> findByParkingMetaAndVehicleType(ParkingMeta parkingMeta, String vehicleType);
}
