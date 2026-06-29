package com.epicraft.pdp.invoice.dto;

import java.time.LocalDate;
import java.util.List;

public record CreateInvoiceRequest(
        String companyId,
        String customerId,
        String periodLabel,
        int workedDays,
        LocalDate issueDate,
        int paymentDelayDays,
        String templateName,
        List<CreateInvoiceLineRequest> lines
) {}
