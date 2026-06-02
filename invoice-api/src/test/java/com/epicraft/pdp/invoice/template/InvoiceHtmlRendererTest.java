package com.epicraft.pdp.invoice.template;

import com.epicraft.pdp.invoice.domain.BankDetails;
import com.epicraft.pdp.invoice.domain.Company;
import com.epicraft.pdp.invoice.domain.Customer;
import com.epicraft.pdp.invoice.domain.Invoice;
import com.epicraft.pdp.invoice.domain.InvoiceLine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceHtmlRendererTest {

    private final InvoiceHtmlRenderer renderer = new InvoiceHtmlRenderer();

    @Test
    void rendersPartyAddressesOnSeparateLinesLikeLegacyFacture004() {
        Company company = new Company("1", "EPICRAFT FRANCE", "55 RUE DE LA FRETTE\n78500 SARTROUVILLE", "", "", "98862388000016", "FR40988623880", new BankDetails("", "FR7640618805000004040056158", "BOUSFRPPXXX", ""));
        Customer customer = new Customer("3", "OCSI", "4 RUE DU COLONEL DRIANT\n75001 PARIS", "", "", "FR64381158575", "38115857500070");
        Invoice invoice = new Invoice(
                null,
                "F-2026-0004",
                "default",
                LocalDate.of(2026, 4, 30),
                LocalDate.of(2026, 6, 14),
                "Avril 2026",
                company,
                customer,
                List.of(new InvoiceLine("Prestation de services IT", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.TEN)),
                BigDecimal.TEN,
                new BigDecimal("0.20"),
                new BigDecimal("2.00"),
                new BigDecimal("12.00"),
                null
        );

        String html = renderer.render(invoice);

        assertThat(html).contains("55 RUE DE LA FRETTE<br/>78500 SARTROUVILLE<br/>");
        assertThat(html).contains("4 RUE DU COLONEL DRIANT<br/>75001 PARIS<br/>");
    }

    @Test
    void escapesPartyAddressesBeforeAddingHtmlLineBreaks() {
        Company company = new Company("1", "EPICRAFT FRANCE", "55 RUE & <LA FRETTE>\n78500 SARTROUVILLE", "", "", "98862388000016", "FR40988623880", new BankDetails("", "", "", ""));
        Customer customer = new Customer("3", "OCSI", "4 RUE \"DU COLONEL\"\n75001 PARIS", "", "", "FR64381158575", "38115857500070");
        Invoice invoice = new Invoice(
                null,
                "F-2026-0004",
                "default",
                LocalDate.of(2026, 4, 30),
                LocalDate.of(2026, 6, 14),
                "Avril 2026",
                company,
                customer,
                List.of(new InvoiceLine("Prestation de services IT", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.TEN)),
                BigDecimal.TEN,
                new BigDecimal("0.20"),
                new BigDecimal("2.00"),
                new BigDecimal("12.00"),
                null
        );

        String html = renderer.render(invoice);

        assertThat(html).contains("55 RUE &amp; &lt;LA FRETTE&gt;<br/>78500 SARTROUVILLE");
        assertThat(html).contains("4 RUE &quot;DU COLONEL&quot;<br/>75001 PARIS");
    }
}
