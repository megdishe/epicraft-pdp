package com.freelance.invoice.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

@Service
public class PdfService {
    public void generatePdf(String html, Path outputPath) {
        try (FileOutputStream os = new FileOutputStream(outputPath.toFile())) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write PDF to " + outputPath, e);
        }
    }
}
