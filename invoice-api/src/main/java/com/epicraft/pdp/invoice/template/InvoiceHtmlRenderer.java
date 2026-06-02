package com.epicraft.pdp.invoice.template;

import com.epicraft.pdp.invoice.domain.Invoice;
import com.epicraft.pdp.invoice.domain.InvoiceLine;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class InvoiceHtmlRenderer {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String render(Invoice invoice) {
        String template = ResourceLoader.load("template/invoice-template.html");
        StringBuilder rows = new StringBuilder();

        for (InvoiceLine line : invoice.lines()) {
            rows.append("<tr><td>%s</td><td class=\"num\">Jour</td><td class=\"num\">%s</td><td class=\"num\">%s €</td><td class=\"num\">%s €</td><td class=\"num\">%s €</td></tr>"
                    .formatted(escape(line.description()), formatNumber(line.quantity()), formatCurrency(line.unitPrice()), formatCurrency(line.total()), formatCurrency(line.total().multiply(java.math.BigDecimal.ONE.add(invoice.vatRate())))));
        }

        return template
                .replace("{{invoiceNumber}}", escape(invoice.invoiceNumber()))
                .replace("{{issueDate}}", invoice.issueDate().format(DATE_FORMATTER))
                .replace("{{dueDate}}", invoice.dueDate().format(DATE_FORMATTER))
                .replace("{{periodLabel}}", escape(invoice.periodLabel()))
                .replace("{{companyName}}", escape(invoice.company().name()))
                .replace("{{companyAddress}}", escapeMultiline(invoice.company().address()))
                .replace("{{companySiret}}", escape(invoice.company().siret()))
                .replace("{{companyVat}}", escape(invoice.company().taxId()))
                .replace("{{clientName}}", escape(invoice.customer().name()))
                .replace("{{clientAddress}}", escapeMultiline(invoice.customer().address()))
                .replace("{{clientSiret}}", escape(invoice.customer().siret()))
                .replace("{{clientVat}}", escape(invoice.customer().taxId()))
                .replace("{{companyIban}}", escape(invoice.company().bankDetails() == null ? "" : invoice.company().bankDetails().iban()))
                .replace("{{companyBic}}", escape(invoice.company().bankDetails() == null ? "" : invoice.company().bankDetails().bic()))
                .replace("{{lineItems}}", rows.toString())
                .replace("{{totalHt}}", formatCurrency(invoice.totalHt()))
                .replace("{{vatAmount}}", formatCurrency(invoice.vatAmount()))
                .replace("{{totalTtc}}", formatCurrency(invoice.totalTtc()));
    }

    private static String formatCurrency(java.math.BigDecimal value) { DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.FRANCE); s.setGroupingSeparator(' '); s.setDecimalSeparator(','); return new DecimalFormat("#,##0.00", s).format(value);}    
    private static String formatNumber(java.math.BigDecimal value) { DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.FRANCE); s.setDecimalSeparator(','); return new DecimalFormat("0.##", s).format(value);}    
    private static String escape(String value) { return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"); }
    private static String escapeMultiline(String value) { return escape(value).replace("\r\n", "\n").replace("\r", "\n").replace("\n", "<br/>"); }
}
