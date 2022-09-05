package org.ankur.parkinglot.fees;

import org.ankur.parkinglot.domain.FeeModel;
import org.ankur.parkinglot.domain.ParkingMeta;
import org.ankur.parkinglot.dto.VehicleType;
import org.ankur.parkinglot.repository.FeeModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.ankur.parkinglot.fees.FeeType.FLAT;
import static org.ankur.parkinglot.fees.FeeType.HRS;

@Component
public class FeesCalculator {

    @Autowired
    private FeeModelRepository feeModelRepository;

    public int calculate(Instant entryTime, Instant exitTime, VehicleType vehicleType, ParkingMeta parkingMeta) {
        List<FeeModel> fees = feeModelRepository.findByParkingMetaAndVehicleType(parkingMeta, vehicleType.name());
        int duration = (int) Duration.between(entryTime, exitTime).toHours();
        int totalFee = 0;
        for (FeeModel f : fees) {
            if (f.getStartingHour() <= duration) {
                if (FLAT.equals(f.getMode())) {
                    totalFee += f.getFee();
                } else if (HRS.equals(f.getMode())) {
                    int hrs = 0;
                    if (f.getEndingHour() <= duration) {
                        hrs = f.getEndingHour() - f.getStartingHour();
                    } else {
                        hrs = duration - f.getStartingHour();
                    }
                    if (hrs == 0) {
                        hrs = 1;
                    }
                    totalFee = totalFee + (hrs * f.getFee());
                }
            }
            if (duration < f.getEndingHour()) {
                break;
            }
        }
        return totalFee;
    }
}


