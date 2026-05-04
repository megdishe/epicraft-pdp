package com.freelance.invoice.service;

import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;

public class SequenceService {
    private final AtomicInteger sequence = new AtomicInteger(0);

    public String nextInvoiceNumber() {
        int next = sequence.incrementAndGet();
        return String.format("F-%d-%04d", Year.now().getValue(), next);
    }
}
