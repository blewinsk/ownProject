package blewinsk.own.project.commons.exceptions;

public class NoSuchBankException extends Throwable {

    public NoSuchBankException(String bankName) {
        super("No such bank: " + bankName);
    }
}
