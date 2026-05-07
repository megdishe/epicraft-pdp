package com.freelance.invoice.controller;

import com.freelance.invoice.domain.Invoice;
import com.freelance.invoice.dto.CreateInvoiceRequest;
import com.freelance.invoice.repository.InvoiceRepository;
import com.freelance.invoice.service.InvoiceService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;
    public InvoiceController(InvoiceService invoiceService, InvoiceRepository invoiceRepository) { this.invoiceService = invoiceService; this.invoiceRepository = invoiceRepository; }
    @PostMapping public Invoice create(@RequestBody CreateInvoiceRequest request) { return invoiceService.generate(request); }
    @GetMapping public List<Invoice> findAll() { return invoiceRepository.findAll(); }

    @PostMapping("/temporary/legacy")
    public Invoice recreateLegacyInvoice(@RequestParam int month, @RequestParam int year, @RequestParam int numberOfDays) {
        return invoiceService.generateLegacy(month, year, numberOfDays);
    }

    @GetMapping("/{id}") public Invoice findById(@PathVariable String id) { return invoiceRepository.findById(id).orElseThrow(); }
}
