package com.freelance.invoice.service;

import com.freelance.invoice.domain.Invoice;
import com.freelance.invoice.domain.InvoiceLine;
import com.freelance.invoice.domain.Party;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class InvoiceService {
    private static final BigDecimal VAT_RATE = new BigDecimal("0.20");
    private static final BigDecimal DAILY_RATE = new BigDecimal("610");

    private final SequenceService sequenceService;

    public InvoiceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    public Invoice createMonthlyInvoice(
            Party company,
            Party client,
            String periodLabel,
            int workedDays,
            LocalDate issueDate,
            int paymentDelayDays
    ) {
        BigDecimal quantity = BigDecimal.valueOf(workedDays);
        BigDecimal totalHt = DAILY_RATE.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatAmount = totalHt.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalTtc = totalHt.add(vatAmount).setScale(2, RoundingMode.HALF_UP);

        InvoiceLine line = new InvoiceLine(
                "Prestations freelance - " + periodLabel,
                quantity,
                DAILY_RATE,
                totalHt
        );

        return new Invoice(
                sequenceService.nextInvoiceNumber(),
                issueDate,
                issueDate.plusDays(paymentDelayDays),
                periodLabel,
                company,
                client,
                List.of(line),
                totalHt,
                VAT_RATE,
                vatAmount,
                totalTtc
        );
    }
}
