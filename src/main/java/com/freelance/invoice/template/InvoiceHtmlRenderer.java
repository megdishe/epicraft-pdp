package com.freelance.invoice.template;

import com.freelance.invoice.domain.Invoice;
import com.freelance.invoice.domain.InvoiceLine;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InvoiceHtmlRenderer {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String render(Invoice invoice) {
        String template = ResourceLoader.load("template/invoice-template.html");
        StringBuilder rows = new StringBuilder();

        for (InvoiceLine line : invoice.lines()) {
            rows.append("""
                    <tr>
                        <td>%s</td>
                        <td class=\"num\">%s</td>
                        <td class=\"num\">%s €</td>
                        <td class=\"num\">%s €</td>
                    </tr>
                    """.formatted(
                    escape(line.description()),
                    formatNumber(line.quantity()),
                    formatCurrency(line.unitPrice()),
                    formatCurrency(line.total())));
        }

        return template
                .replace("{{invoiceNumber}}", escape(invoice.invoiceNumber()))
                .replace("{{issueDate}}", invoice.issueDate().format(DATE_FORMATTER))
                .replace("{{dueDate}}", invoice.dueDate().format(DATE_FORMATTER))
                .replace("{{periodLabel}}", escape(invoice.periodLabel()))
                .replace("{{companyName}}", escape(invoice.company().name()))
                .replace("{{companyAddress}}", escape(invoice.company().address()))
                .replace("{{companyPostalCode}}", escape(invoice.company().postalCode()))
                .replace("{{companyCity}}", escape(invoice.company().city()))
                .replace("{{companyCountry}}", escape(invoice.company().country()))
                .replace("{{companySiret}}", escape(invoice.company().siret()))
                .replace("{{companyVat}}", escape(invoice.company().vatNumber()))
                .replace("{{companyEmail}}", escape(invoice.company().email()))
                .replace("{{clientName}}", escape(invoice.client().name()))
                .replace("{{clientAddress}}", escape(invoice.client().address()))
                .replace("{{clientPostalCode}}", escape(invoice.client().postalCode()))
                .replace("{{clientCity}}", escape(invoice.client().city()))
                .replace("{{clientCountry}}", escape(invoice.client().country()))
                .replace("{{lineItems}}", rows.toString())
                .replace("{{totalHt}}", formatCurrency(invoice.totalHt()))
                .replace("{{vatRate}}", formatNumber(invoice.vatRate().multiply(java.math.BigDecimal.valueOf(100))))
                .replace("{{vatAmount}}", formatCurrency(invoice.vatAmount()))
                .replace("{{totalTtc}}", formatCurrency(invoice.totalTtc()));
    }

    private static String formatCurrency(java.math.BigDecimal value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
        return format.format(value);
    }

    private static String formatNumber(java.math.BigDecimal value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("0.##", symbols);
        return format.format(value);
    }

    private static String escape(String value) {
        return value == null ? "" : value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
