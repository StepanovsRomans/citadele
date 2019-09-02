package lv.citadele.paymentformcore.utils;

import com.itextpdf.text.pdf.BaseFont;
import lv.citadele.paymentformcore.exceptions.FileNullPointerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class PdfGeneratorUtil {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TemplateEngine templateEngine;

    public File createPdf(Map map) {
        LOGGER.info("Trying to create PDF...");
        Context thymeleafContext = new Context();

        for (Object mapEntry : map.entrySet()) {
            Map.Entry pair = (Map.Entry) mapEntry;
            thymeleafContext.setVariable(pair.getKey().toString(), pair.getValue());
        }

        final ClassPathResource regular = new ClassPathResource("fonts/ArialRegular.ttf");

        String processedHtml = templateEngine.process("payment_form", thymeleafContext);

        try {
            String fileName = UUID.randomUUID().toString();
            File tempFile = File.createTempFile(fileName, ".pdf");
            try (FileOutputStream os = new FileOutputStream(tempFile)) {
                ITextRenderer renderer = new ITextRenderer();
                renderer.getFontResolver().addFont(regular.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                renderer.setDocumentFromString(processedHtml);
                renderer.layout();
                renderer.createPDF(os, false);
                renderer.finishPDF();
                LOGGER.info("PDF created successfully");

                return tempFile;
            }
        } catch (IOException | com.lowagie.text.DocumentException e) {
            throw new FileNullPointerException("File is Empty", e);
        }
    }
}
