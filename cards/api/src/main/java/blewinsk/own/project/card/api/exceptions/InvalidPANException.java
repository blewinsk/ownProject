package blewinsk.own.project.card.api.exceptions;

public class InvalidPANException extends RuntimeException {

    public InvalidPANException(String panNumber) {
        super("Invalid PAN number " + panNumber);
    }
}
