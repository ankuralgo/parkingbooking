package org.ankur.parkinglot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ExitInvoiceDTO {

    private String receiptNo;
    private Instant entryTime;
    private Instant exitTime;
    private float fees;
}
