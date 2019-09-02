package lv.citadele.paymentformcore.exceptions;

public class FileNullPointerException extends RuntimeException {
    public FileNullPointerException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
