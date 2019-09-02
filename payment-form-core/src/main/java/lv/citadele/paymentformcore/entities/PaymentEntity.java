package lv.citadele.paymentformcore.entities;

import lombok.Builder;
import lombok.Data;
import lv.citadele.paymentformcore.enums.PaymentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
public class PaymentEntity {

    @NotBlank(message = "Client Number is mandatory")
    @Size(max = 5)
    private String clientNumber;

    @NotNull
    private Date date;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 30)
    private String remitterName;

    @NotBlank(message = "Personal id is mandatory")
    @Size(max = 15)
    private String remitterPersonalId;

    @NotBlank(message = "Account number is mandatory")
    @Size(max = 15)
    private String remitterAccountNo;

    @NotBlank(message = "Beneficiary name is mandatory")
    @Size(max = 30)
    private String beneficiaryName;

    @NotBlank(message = "Personal id is mandatory")
    @Size(max = 15)
    private String beneficiaryPersonalId;

    @NotBlank(message = "Account number is mandatory")
    @Size(max = 15)
    private String beneficiaryAccountNo;

    @NotBlank(message = "Residence Country is mandatory")
    @Size(max = 20)
    private String beneficiaryResidenceCountry;

    @NotBlank(message = "Beneficiary bank is mandatory")
    @Size(max = 40)
    private String beneficiaryBank;

    @Size(max = 10)
    @NotBlank(message = "Beneficiary bank code is mandatory")
    private String beneficiaryBankCode;

    @Size(max = 15)
    @NotBlank(message = "Amount is mandatory")
    private String amount;

    @NotBlank(message = "Amount in words is mandatory")
    private String amountInWords;

    private PaymentType paymentType;

    @Size(max = 10)
    @NotBlank(message = "Exchange rate is mandatory")
    private String exchangeRate;

    @Size(max = 15)
    @NotBlank(message = "Bank fee is mandatory")
    private String bankFee;

    @NotNull(message = "Value date is mandatory")
    private Date valueDate;

    @Size(max = 4, min = 4)
    @NotBlank(message = "Code of External Payment is mandatory")
    private String codeOfExternalPayment;

    @Size(max = 144)
    @NotBlank(message = "Payment details is mandatory")
    private String paymentDetails;

    public String getSimpleDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public String getSimpleValueDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(valueDate);
    }
}
