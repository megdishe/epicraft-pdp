package com.freelance.invoice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document("invoices")
public record Invoice(
        @Id String id,
        String invoiceNumber,
        String templateName,
        LocalDate issueDate,
        LocalDate dueDate,
        String periodLabel,
        Company company,
        Customer customer,
        List<InvoiceLine> lines,
        BigDecimal totalHt,
        BigDecimal vatRate,
        BigDecimal vatAmount,
        BigDecimal totalTtc,
        String pdfPath
) {}
