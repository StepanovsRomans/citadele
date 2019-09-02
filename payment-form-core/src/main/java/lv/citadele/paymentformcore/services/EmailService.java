package lv.citadele.paymentformcore.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.citadele.paymentformcore.entities.PaymentEntity;
import lv.citadele.paymentformcore.exceptions.FileNullPointerException;
import lv.citadele.paymentformcore.exceptions.MailException;
import lv.citadele.paymentformcore.utils.PdfGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {

    public static final String EMAIL_SUBJECT = "Payment Form";
    public static final String EMAIL_TEXT = "<h1>Please check attachments for files.</h1>";
    public static final String DOCUMENT_NAME = "document.pdf";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PdfGeneratorUtil pdfGeneratorUtil;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${receiver.email.address}")
    private String receiverEmailAddress;

    public void sendEmail(PaymentEntity paymentEntity) throws FileNullPointerException {
        Map paymentContent = populatePaymentContent(paymentEntity);
        File filePDF = pdfGeneratorUtil.createPdf(paymentContent);
        sendEmailWithAttachment(filePDF);
    }

    void sendEmailWithAttachment(File attachment) throws MailException {
        LOGGER.info("Sending e-mail with attachment...");
        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(receiverEmailAddress);
            helper.setSubject(EMAIL_SUBJECT);
            helper.setText(EMAIL_TEXT, true);
            helper.addAttachment(DOCUMENT_NAME, attachment);

            javaMailSender.send(msg);
        } catch (MessagingException e) {
            throw new MailException("Mail hasn't been sent", e);
        }
    }

    Map populatePaymentContent(PaymentEntity paymentEntity) {
        Map paymentContent = new ObjectMapper().convertValue(paymentEntity, Map.class);

        paymentContent.replace("date", paymentEntity.getSimpleDate());
        paymentContent.replace("valueDate", paymentEntity.getSimpleValueDate());

        return paymentContent;
    }
}
