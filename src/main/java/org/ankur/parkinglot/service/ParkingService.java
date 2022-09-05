package org.ankur.parkinglot.service;

import org.ankur.parkinglot.dto.EntryTicketDTO;
import org.ankur.parkinglot.dto.ExitInvoiceDTO;
import org.ankur.parkinglot.dto.VehicleType;

import javax.transaction.Transactional;

public interface ParkingService {

    @Transactional
    EntryTicketDTO generateEntryTicket(long parkingMetaid, VehicleType vehicleType);

    @Transactional
    ExitInvoiceDTO generateExitReceipt(long ticketNo);
}
