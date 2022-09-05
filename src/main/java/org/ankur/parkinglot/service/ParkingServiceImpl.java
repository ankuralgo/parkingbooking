package org.ankur.parkinglot.service;

import org.ankur.parkinglot.domain.EntryTicket;
import org.ankur.parkinglot.domain.Invoice;
import org.ankur.parkinglot.domain.ParkingMeta;
import org.ankur.parkinglot.domain.ParkingSlot;
import org.ankur.parkinglot.dto.EntryTicketDTO;
import org.ankur.parkinglot.dto.ExitInvoiceDTO;
import org.ankur.parkinglot.dto.VehicleType;
import org.ankur.parkinglot.fees.FeesCalculator;
import org.ankur.parkinglot.repository.EntryTicketRepository;
import org.ankur.parkinglot.repository.InvoiceRepository;
import org.ankur.parkinglot.repository.ParkingMetaRepository;
import org.ankur.parkinglot.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private EntryTicketRepository entryTicketRepository;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FeesCalculator feesCalculator;

    @Autowired
    private ParkingMetaRepository parkingMetaRepository;

    @Transactional
    @Override
    public EntryTicketDTO generateEntryTicket(long parkingMetaId, VehicleType vehicleType) {
        Optional<ParkingMeta> parkingMeta = parkingMetaRepository.findById(parkingMetaId);

        ParkingSlot slot = parkingSlotRepository.findFirstByVehicleTypeAndOccupiedAndParkingMetaOrderById(vehicleType.name(), false, parkingMeta.get());
        if (Objects.isNull(slot)) {
            throw new RuntimeException("No slot available");
        }

        EntryTicket entryTicket = new EntryTicket();
        long entryTime = Instant.now().toEpochMilli();
        entryTicket.setEntryTime(entryTime);
        entryTicket.setSlotNo(slot);
        entryTicket.setVehicleType(vehicleType.name());

        EntryTicket savedEntryTicket = entryTicketRepository.save(entryTicket);

        slot.setOccupied(true);
        parkingSlotRepository.save(slot);

        EntryTicketDTO entryTicketDTO = EntryTicketDTO.builder().ticketNo(savedEntryTicket.getId())
                .entryTime(Instant.ofEpochMilli(entryTime)).slotNumber(slot.getSpotNumber()).vehicleType(vehicleType).build();
        return entryTicketDTO;

    }

    @Override
    @Transactional
    public ExitInvoiceDTO generateExitReceipt(long ticketNo) {

        Optional<EntryTicket> ticket = entryTicketRepository.findById(ticketNo);

        EntryTicket tkt = ticket.orElseThrow(() -> {
            return new RuntimeException("Invalid ticket");
        });
        Instant entryTime = Instant.ofEpochMilli(tkt.getEntryTime());
        Instant exitTime = Instant.now();
        float fees = feesCalculator.calculate(entryTime, Instant.now(), VehicleType.valueOf(tkt.getVehicleType()), tkt.getSlotNo().getParkingMeta());
        Invoice invoice = new Invoice();
        invoice.setExitTime(exitTime);
        invoice.setFees(fees);
        invoice.setTicket(ticket.get());
        Invoice savedInvoice = invoiceRepository.save(invoice);
        ExitInvoiceDTO exitInvoice = ExitInvoiceDTO.builder().exitTime(exitTime).entryTime(entryTime).fees(fees).receiptNo(String.valueOf(savedInvoice.getId())).build();
        return exitInvoice;


    }
}
