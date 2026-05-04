package com.freelance.invoice.domain;

import java.math.BigDecimal;

public record InvoiceLine(
        String description,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal total
) {
}
