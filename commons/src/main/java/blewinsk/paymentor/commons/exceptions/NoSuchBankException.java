package blewinsk.paymentor.commons.exceptions;

public class NoSuchBankException extends RuntimeException {

    public NoSuchBankException(String bankName) {
        super("No such bank: " + bankName);
    }
}
