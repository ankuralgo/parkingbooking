package org.ankur.parkinglot;

import org.ankur.parkinglot.domain.EntryTicket;
import org.ankur.parkinglot.domain.ParkingSlot;
import org.ankur.parkinglot.dto.*;
import org.ankur.parkinglot.repository.EntryTicketRepository;
import org.ankur.parkinglot.repository.ParkingMetaRepository;
import org.ankur.parkinglot.repository.ParkingSlotRepository;
import org.ankur.parkinglot.service.OnboardingService;
import org.ankur.parkinglot.service.ParkingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ankur.parkinglot.dto.VehicleType.*;
import static org.ankur.parkinglot.fees.FeeType.FLAT;
import static org.ankur.parkinglot.fees.FeeType.HRS;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ParkinglotApplicationTests {


    FeedModelDTO fee1 = new FeedModelDTO(0, Integer.MAX_VALUE, 10, 1, FLAT, TWO_WHEELER);
    FeedModelDTO fee2 = new FeedModelDTO(0, Integer.MAX_VALUE, 20, 1, FLAT, CAR);
    FeedModelDTO fee3 = new FeedModelDTO(0, Integer.MAX_VALUE, 50, 1, FLAT, BUS);


    @LocalServerPort
    private int port;
    @Autowired
    private OnboardingService onboardingService;
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ParkingMetaRepository parkingMetaRepository;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    @Autowired
    private EntryTicketRepository entryTicketRepository;

    @Test
    void testOnboardParking() {


        Map<VehicleType, Integer> slots = new HashMap<>();
        slots.put(CAR, 2);
        slots.put(TWO_WHEELER, 2);
        ParkingMetaDTO parkingMetaDTO = ParkingMetaDTO.builder().parkingType(ParkingType.MALL).city("Pune").area("Baner")
                .feeModel(Arrays.asList(fee1, fee2, fee3))
                .vehicleSlots(slots).build();

        long parking = onboardingService.onboardParking(parkingMetaDTO);
        Assertions.assertEquals(parkingMetaRepository.findAll().size(), 1);

        List<ParkingSlot> savedSlots = parkingSlotRepository.findAll();
        int carSlots = 0;
        int twoWheelerSlots = 0;

        for (ParkingSlot s : savedSlots) {
            if (CAR.name().equals(s.getVehicleType())) {
                carSlots++;
            } else if (TWO_WHEELER.name().equals(s.getVehicleType())) {
                twoWheelerSlots++;
            }
        }

        Assertions.assertEquals(2, carSlots);
        Assertions.assertEquals(2, twoWheelerSlots);


    }

    @Test
    void testOneBooking() {
        Map<VehicleType, Integer> slots = new HashMap<>();
        slots.put(CAR, 2);
        slots.put(TWO_WHEELER, 2);
        ParkingMetaDTO parkingMetaDTO = ParkingMetaDTO.builder().parkingType(ParkingType.MALL).city("Pune").area("Baner").feeModel(Arrays.asList(fee1, fee2, fee3)).vehicleSlots(slots).build();
        long parking = onboardingService.onboardParking(parkingMetaDTO);
        Assertions.assertTrue(parking > 0);
        EntryTicketDTO slot = parkingService.generateEntryTicket(parking, CAR);
        Assertions.assertEquals(CAR, slot.getVehicleType());
        Assertions.assertEquals(0, Duration.between(slot.getEntryTime(), Instant.now()).toMinutes())
        ;
    }


    @Test
    void testMultipleBooking() {
        Map<VehicleType, Integer> slots = new HashMap<>();
        slots.put(CAR, 2);
        slots.put(TWO_WHEELER, 2);
        ParkingMetaDTO parkingMetaDTO = ParkingMetaDTO.builder().parkingType(ParkingType.MALL).city("Pune").area("Baner").feeModel(Arrays.asList(fee1, fee2, fee3)).vehicleSlots(slots).build();
        long parking = onboardingService.onboardParking(parkingMetaDTO);
        Assertions.assertTrue(parking > 0);
        EntryTicketDTO slot = parkingService.generateEntryTicket(parking, CAR);
        Assertions.assertEquals(CAR, slot.getVehicleType());
        Assertions.assertEquals(0, Duration.between(slot.getEntryTime(), Instant.now()).toMinutes());

        slot = parkingService.generateEntryTicket(parking, CAR);
        Assertions.assertEquals(CAR, slot.getVehicleType());
        Assertions.assertEquals(0, Duration.between(slot.getEntryTime(), Instant.now()).toMinutes());
    }

    @Test
    void testNoSlotAvailable() {
        Map<VehicleType, Integer> slots = new HashMap<>();
        slots.put(CAR, 2);
        slots.put(TWO_WHEELER, 2);
        ParkingMetaDTO parkingMetaDTO = ParkingMetaDTO.builder().parkingType(ParkingType.MALL).city("Pune").area("Baner").feeModel(Arrays.asList(fee1, fee2, fee3)).vehicleSlots(slots).build();
        long parking = onboardingService.onboardParking(parkingMetaDTO);
        Assertions.assertTrue(parking > 0);
        parkingService.generateEntryTicket(parking, CAR);
        parkingService.generateEntryTicket(parking, CAR);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            parkingService.generateEntryTicket(parking, CAR);

        });

        Assertions.assertEquals("No slot available", exception.getMessage());

    }


    @Test
    void testInvoice() {
        Map<VehicleType, Integer> slots = new HashMap<>();
        slots.put(CAR, 2);
        slots.put(TWO_WHEELER, 2);
        ParkingMetaDTO parkingMetaDTO = ParkingMetaDTO.builder().parkingType(ParkingType.MALL).city("Pune").area("Baner").feeModel(Arrays.asList(fee1, fee2, fee3)).vehicleSlots(slots).build();
        long parking = onboardingService.onboardParking(parkingMetaDTO);
        Assertions.assertTrue(parking > 0);
        EntryTicketDTO slot = parkingService.generateEntryTicket(parking, CAR);
        ExitInvoiceDTO invoice = parkingService.generateExitReceipt(slot.getTicketNo());
        System.out.println(slot);
        System.out.println(invoice);
    }


    @Test
    void testFlatFeesInvoice() {
        Map<VehicleType, Integer> slots = new HashMap<>();
        slots.put(CAR, 2);
        slots.put(TWO_WHEELER, 2);
        ParkingMetaDTO parkingMetaDTO = ParkingMetaDTO.builder().parkingType(ParkingType.MALL).city("Pune").area("Baner").feeModel(Arrays.asList(fee1, fee2, fee3)).vehicleSlots(slots).build();
        long parking = onboardingService.onboardParking(parkingMetaDTO);
        Assertions.assertTrue(parking > 0);
        EntryTicketDTO slot = parkingService.generateEntryTicket(parking, CAR);
        ExitInvoiceDTO invoice = parkingService.generateExitReceipt(slot.getTicketNo());
        Assertions.assertEquals(20, invoice.getFees());
        slot = parkingService.generateEntryTicket(parking, TWO_WHEELER);
        invoice = parkingService.generateExitReceipt(slot.getTicketNo());
        Assertions.assertEquals(10, invoice.getFees());
    }


    @Test
    void testHrsFeesInvoice() {

        FeedModelDTO feeh1 = new FeedModelDTO(0, 1, 10, 1, HRS, TWO_WHEELER);
        FeedModelDTO feeh2 = new FeedModelDTO(1, 8, 40, 2, HRS, TWO_WHEELER);
        FeedModelDTO feeh3 = new FeedModelDTO(8, 24, 60, 3, HRS, TWO_WHEELER);
        FeedModelDTO feeh4 = new FeedModelDTO(24, Integer.MAX_VALUE, 80, 4, HRS, TWO_WHEELER);
        Map<VehicleType, Integer> slots = new HashMap<>();
        slots.put(CAR, 2);
        slots.put(TWO_WHEELER, 2);
        ParkingMetaDTO parkingMetaDTO = ParkingMetaDTO.builder().parkingType(ParkingType.MALL).city("Pune").area("Baner").feeModel(Arrays.asList(feeh1, feeh2, feeh3)).vehicleSlots(slots).build();
        long parking = onboardingService.onboardParking(parkingMetaDTO);
        Assertions.assertTrue(parking > 0);
        EntryTicketDTO slot = parkingService.generateEntryTicket(parking, TWO_WHEELER);
        ExitInvoiceDTO invoice = parkingService.generateExitReceipt(slot.getTicketNo());
        Assertions.assertEquals(10, invoice.getFees());


    }

    @Test
    void testMultipleHrsFeesInvoice() {

        FeedModelDTO feeh1 = new FeedModelDTO(0, 1, 0, 1, HRS, TWO_WHEELER);
        FeedModelDTO feeh2 = new FeedModelDTO(1, 8, 40, 2, HRS, TWO_WHEELER);
        FeedModelDTO feeh3 = new FeedModelDTO(8, 24, 60, 3, HRS, TWO_WHEELER);
        FeedModelDTO feeh4 = new FeedModelDTO(24, Integer.MAX_VALUE, 80, 4, HRS, TWO_WHEELER);
        Map<VehicleType, Integer> slots = new HashMap<>();
        slots.put(CAR, 2);
        slots.put(TWO_WHEELER, 2);
        ParkingMetaDTO parkingMetaDTO = ParkingMetaDTO.builder().parkingType(ParkingType.MALL).city("Pune").area("Baner").feeModel(Arrays.asList(feeh1, feeh2, feeh3, feeh4)).vehicleSlots(slots).build();
        long parking = onboardingService.onboardParking(parkingMetaDTO);
        Assertions.assertTrue(parking > 0);
        EntryTicketDTO slot = parkingService.generateEntryTicket(parking, TWO_WHEELER);

        EntryTicket tkt = entryTicketRepository.findById(slot.getTicketNo()).get();
        tkt.setEntryTime(Instant.now().minus(10, ChronoUnit.HOURS).toEpochMilli());
        entryTicketRepository.save(tkt);

        ExitInvoiceDTO invoice = parkingService.generateExitReceipt(slot.getTicketNo());
        Assertions.assertEquals(400, invoice.getFees());

    }


}
