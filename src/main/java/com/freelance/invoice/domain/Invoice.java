package com.freelance.invoice.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record Invoice(
        String invoiceNumber,
        LocalDate issueDate,
        LocalDate dueDate,
        String periodLabel,
        Party company,
        Party client,
        List<InvoiceLine> lines,
        BigDecimal totalHt,
        BigDecimal vatRate,
        BigDecimal vatAmount,
        BigDecimal totalTtc
) {
}
