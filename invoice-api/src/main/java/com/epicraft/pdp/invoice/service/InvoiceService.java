package com.epicraft.pdp.invoice.service;

import com.epicraft.pdp.invoice.domain.*;
import com.epicraft.pdp.invoice.dto.CreateInvoiceRequest;
import com.epicraft.pdp.invoice.repository.CompanyRepository;
import com.epicraft.pdp.invoice.repository.CustomerRepository;
import com.epicraft.pdp.invoice.repository.InvoiceRepository;
import com.epicraft.pdp.invoice.template.InvoiceHtmlRenderer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InvoiceService {
    private static final BigDecimal VAT_RATE = new BigDecimal("0.20");
    private static final BigDecimal DAILY_RATE = new BigDecimal("610");
    private final AtomicInteger sequence = new AtomicInteger(4);
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceHtmlRenderer renderer;
    private final PdfService pdfService;

    public InvoiceService(CompanyRepository companyRepository, CustomerRepository customerRepository, InvoiceRepository invoiceRepository, InvoiceHtmlRenderer renderer, PdfService pdfService) {
        this.companyRepository = companyRepository; this.customerRepository = customerRepository; this.invoiceRepository = invoiceRepository; this.renderer = renderer; this.pdfService = pdfService;
    }

    public Invoice generateLegacy(CreateInvoiceRequest request) {
        Company company = new Company("1", "EPICRAFT FRANCE","55 RUE DE LA FRETTE\n" +
                "78500 SARTROUVILLE","", "", "98862388000016", "FR40988623880",new BankDetails("", "FR7640618805000004040056158", "BOUSFRPPXXX",""));
        Customer customer =new Customer("3", "OCSI","4 RUE DU COLONEL DRIANT\n" +
                "75001 PARIS", "", "", "FR64381158575","38115857500070");
        String template = request.templateName() == null || request.templateName().isBlank() ? "default" : request.templateName();

        BigDecimal quantity = BigDecimal.valueOf(request.workedDays());
        BigDecimal totalHt = DAILY_RATE.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatAmount = totalHt.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalTtc = totalHt.add(vatAmount).setScale(2, RoundingMode.HALF_UP);
        String invoiceNumber = String.format("F-%d-%04d", Year.now().getValue(), sequence.incrementAndGet());

        Invoice invoice = new Invoice(null, invoiceNumber, template, request.issueDate(), request.issueDate().plusDays(request.paymentDelayDays()), request.periodLabel(), company, customer,
                List.of(new InvoiceLine("Prestation de services IT (mission Banque de France) " + request.periodLabel(), quantity, DAILY_RATE, totalHt)), totalHt, VAT_RATE, vatAmount, totalTtc, null);

        Path output = resolveInvoiceOutputPath(invoice.invoiceNumber());
        pdfService.generatePdf(renderer.render(invoice), output);
        Invoice persisted = new Invoice(null, invoice.invoiceNumber(), template, invoice.issueDate(), invoice.dueDate(), invoice.periodLabel(), company, customer, invoice.lines(), totalHt, VAT_RATE, vatAmount, totalTtc, output.toString());
        return invoiceRepository.save(persisted);
    }

    public Invoice generate(CreateInvoiceRequest request) {
        Company company = companyRepository.findById(request.companyId()).orElseThrow();
        Customer customer = customerRepository.findById(request.customerId()).orElseThrow();
        String template = request.templateName() == null || request.templateName().isBlank() ? "default" : request.templateName();

        BigDecimal quantity = BigDecimal.valueOf(request.workedDays());
        BigDecimal totalHt = DAILY_RATE.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatAmount = totalHt.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalTtc = totalHt.add(vatAmount).setScale(2, RoundingMode.HALF_UP);
        String invoiceNumber = String.format("F-%d-%04d", Year.now().getValue(), sequence.incrementAndGet());

        Invoice invoice = new Invoice(null, invoiceNumber, template, request.issueDate(), request.issueDate().plusDays(request.paymentDelayDays()), request.periodLabel(), company, customer,
                List.of(new InvoiceLine("Prestation de services IT (mission Banque de France) " + request.periodLabel(), quantity, DAILY_RATE, totalHt)), totalHt, VAT_RATE, vatAmount, totalTtc, null);

        Path output = resolveInvoiceOutputPath(invoice.invoiceNumber());
        pdfService.generatePdf(renderer.render(invoice), output);
        Invoice persisted = new Invoice(null, invoice.invoiceNumber(), template, invoice.issueDate(), invoice.dueDate(), invoice.periodLabel(), company, customer, invoice.lines(), totalHt, VAT_RATE, vatAmount, totalTtc, output.toString());
        return invoiceRepository.save(persisted);
    }


    public Invoice generateLegacy(int month, int year, int numberOfDays) {
        DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", java.util.Locale.FRENCH);
        String periodLabel = java.time.LocalDate.of(year, month, 1).format(formatter);
        periodLabel = periodLabel.substring(0, 1).toUpperCase(java.util.Locale.FRENCH) + periodLabel.substring(1);
        CreateInvoiceRequest request = new CreateInvoiceRequest(
              null,
              null,
                periodLabel,
                numberOfDays,
                java.time.LocalDate.of(year, month, java.time.Month.of(month).length(java.time.Year.isLeap(year))),
                45,
                "default"
        );

        return generateLegacy(request);
    }
    private Path resolveInvoiceOutputPath(String invoiceName) {
        try { Files.createDirectories(Path.of("invoices")); } catch (Exception e) { throw new IllegalStateException(e); }
        Path c = Path.of("invoices", invoiceName + ".pdf"); int i=1; while (Files.exists(c)) { c = Path.of("invoices", invoiceName + "-" + i++ + ".pdf"); } return c;
    }
}
