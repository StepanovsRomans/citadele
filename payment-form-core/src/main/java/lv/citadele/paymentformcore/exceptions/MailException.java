package lv.citadele.paymentformcore.exceptions;

import javax.mail.MessagingException;

public class MailException extends RuntimeException {
    public MailException(String errorMessage, MessagingException e) {
        super(errorMessage, e);
    }
}
