package com.freelance.invoice;

import com.freelance.invoice.domain.Party;
import com.freelance.invoice.service.InvoiceService;
import com.freelance.invoice.service.PdfService;
import com.freelance.invoice.service.SequenceService;
import com.freelance.invoice.template.InvoiceHtmlRenderer;

import java.nio.file.Path;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Party company = new Party(
                "Jean Dupont EI",
                "15 Rue de Rivoli",
                "75001",
                "Paris",
                "France",
                "123 456 789 00012",
                "FR12123456789",
                "contact@jeandupont.dev"
        );

        Party client = new Party(
                "Tech Client SAS",
                "22 Avenue de l'Opéra",
                "75002",
                "Paris",
                "France",
                "",
                "",
                "finance@techclient.fr"
        );

        SequenceService sequenceService = new SequenceService();
        InvoiceService invoiceService = new InvoiceService(sequenceService);
        InvoiceHtmlRenderer htmlRenderer = new InvoiceHtmlRenderer();
        PdfService pdfService = new PdfService();

        var invoice = invoiceService.createMonthlyInvoice(
                company,
                client,
                "Avril 2026",
                21,
                LocalDate.of(2026, 4, 30),
                45
        );

        String html = htmlRenderer.render(invoice);
        Path output = Path.of("output", invoice.invoiceNumber() + ".pdf");
        pdfService.generatePdf(html, output);

        System.out.println("Invoice generated: " + output.toAbsolutePath());
    }
}
