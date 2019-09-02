package lv.citadele.paymentformcore.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum PaymentType {
    @JsonProperty("Cash")
    CASH("Cash"),

    @JsonProperty("Cheque")
    CHEQUE("Cheque"),

    @JsonProperty("Debit")
    DEBIT("Debit"),

    @JsonProperty("Credit")
    CREDIT("Credit");


    private final String paymentType;

    private PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
