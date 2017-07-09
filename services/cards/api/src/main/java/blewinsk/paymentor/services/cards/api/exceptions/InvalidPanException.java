package blewinsk.paymentor.services.cards.api.exceptions;

public class InvalidPanException extends RuntimeException {

    public InvalidPanException(String panNumber) {
        super("Invalid PAN number " + panNumber);
    }
}
