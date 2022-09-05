package org.ankur.parkinglot.service;

import org.ankur.parkinglot.domain.FeeModel;
import org.ankur.parkinglot.domain.ParkingMeta;
import org.ankur.parkinglot.domain.ParkingSlot;
import org.ankur.parkinglot.dto.FeedModelDTO;
import org.ankur.parkinglot.dto.ParkingMetaDTO;
import org.ankur.parkinglot.repository.FeeModelRepository;
import org.ankur.parkinglot.repository.ParkingMetaRepository;
import org.ankur.parkinglot.repository.ParkingSlotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class OnboardingServiceImpl implements OnboardingService {

    @Autowired
    private ParkingMetaRepository parkingMetaRepository;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private FeeModelRepository feeModelRepository;


    @Override
    @Transactional
    public long onboardParking(ParkingMetaDTO parking) {
        ParkingMeta savedParkingMeta = saveParkingMeta(parking);
        saveParkingLot(parking, savedParkingMeta);
        saveFeeModel(parking.getFeeModel(), savedParkingMeta);
        return savedParkingMeta.getId();
    }

    private ParkingMeta saveParkingMeta(ParkingMetaDTO parking) {
        ParkingMeta parkingMeta = new ParkingMeta();
        BeanUtils.copyProperties(parking, parkingMeta);
        parkingMeta.setParkingType(parking.getParkingType().name());
        ParkingMeta savedParkingMeta = parkingMetaRepository.save(parkingMeta);
        return savedParkingMeta;
    }

    private void saveParkingLot(ParkingMetaDTO parking, ParkingMeta savedParking) {
        parking.getVehicleSlots().forEach((k, v) -> {
            for (int i = 1; i <= v; i++) {
                ParkingSlot slot = new ParkingSlot();
                slot.setParkingMeta(savedParking);
                slot.setSpotNumber(i);
                slot.setVehicleType(k.name());
                parkingSlotRepository.save(slot);
            }
        });
    }


    private void saveFeeModel(List<FeedModelDTO> fees, ParkingMeta savedParking) {

        Collections.sort(fees, new Comparator<FeedModelDTO>() {

            @Override
            public int compare(FeedModelDTO o1, FeedModelDTO o2) {
                int x = o1.getOrder();
                int y = o2.getOrder();
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            }
        });

        fees.stream().forEach(f -> {

            FeeModel feeModel = new FeeModel();
            feeModel.setFee(f.getFee());
            feeModel.setStartingHour(f.getStartingHour());
            feeModel.setEndingHour(f.getEndingHour());
            feeModel.setRuleOrder(f.getOrder());
            feeModel.setVehicleType(f.getVehicleType().name());
            feeModel.setParkingMeta(savedParking);
            feeModel.setMode(f.getFeeType());

            feeModelRepository.save(feeModel); //TODO: we can also use save all method here

        });

    }


    //TODO: We can add more method here to modify the parking meta or slots
}
