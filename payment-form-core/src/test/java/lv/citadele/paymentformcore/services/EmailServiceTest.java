package lv.citadele.paymentformcore.services;

import lv.citadele.paymentformcore.entities.PaymentEntity;
import lv.citadele.paymentformcore.enums.PaymentType;
import lv.citadele.paymentformcore.utils.PdfGeneratorUtil;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    File file;
    @Spy
    @InjectMocks
    private EmailService emailService;
    @Mock
    private PdfGeneratorUtil pdfGeneratorUtil;
    @Mock
    private JavaMailSender javaMailSender;
    private PaymentEntity paymentEntity;
    private MimeMessage message;
    private Session session;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(emailService, "receiverEmailAddress", "test@test.com");
        file = new File("path");
        paymentEntity = buildEmailEntity();
        message = new MimeMessage(session);
    }

    @Test
    public void sendEmailSuccessfulPathTest() {
        doReturn(file).when(pdfGeneratorUtil).createPdf(anyMap());
        doReturn(message).when(javaMailSender).createMimeMessage();
        emailService.sendEmail(paymentEntity);

        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    public void verifyAttributePopulation() {
        Map result = emailService.populatePaymentContent(paymentEntity);

        assertThat(result.get("clientNumber"), is("1"));
        assertThat(result.get("remitterName"), is("Full Name"));
        assertThat(result.get("remitterPersonalId"), is("12122012-21212"));
        assertThat(result.get("remitterAccountNo"), is("123456789"));
        assertThat(result.get("beneficiaryName"), is("Name Full"));
        assertThat(result.get("beneficiaryPersonalId"), is("21212021-12121"));
        assertThat(result.get("beneficiaryAccountNo"), is("987654321"));
        assertThat(result.get("beneficiaryResidenceCountry"), is("Latvia"));
        assertThat(result.get("beneficiaryBank"), is("Citadele"));
        assertThat(result.get("beneficiaryBankCode"), is("CTDLE"));
        assertThat(result.get("amount"), is("1000000 EUR"));
        assertThat(result.get("bankFee"), is("1000000 EUR"));
        assertThat(result.get("amountInWords"), is("One million euro"));
        assertThat(result.get("paymentType"), is(PaymentType.DEBIT.getPaymentType()));
        assertThat(result.get("exchangeRate"), is("1"));
        assertThat(result.get("codeOfExternalPayment"), is("CODE"));
        assertThat(result.get("paymentDetails"), is("Usual payment"));
    }

    private PaymentEntity buildEmailEntity() {
        return PaymentEntity.builder()
                .date(DateUtil.now())
                .clientNumber("1")
                .remitterName("Full Name")
                .remitterPersonalId("12122012-21212")
                .remitterAccountNo("123456789")
                .beneficiaryName("Name Full")
                .beneficiaryPersonalId("21212021-12121")
                .beneficiaryAccountNo("987654321")
                .beneficiaryResidenceCountry("Latvia")
                .beneficiaryBank("Citadele")
                .beneficiaryBankCode("CTDLE")
                .amount("1000000 EUR")
                .bankFee("1000000 EUR")
                .amountInWords("One million euro")
                .paymentType(PaymentType.DEBIT)
                .exchangeRate("1")
                .valueDate(DateUtil.now())
                .codeOfExternalPayment("CODE")
                .paymentDetails("Usual payment")
                .build();
    }
}