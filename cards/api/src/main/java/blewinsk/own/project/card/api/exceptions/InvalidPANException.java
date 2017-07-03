package blewinsk.own.project.card.api.exceptions;

public class InvalidPANException extends Throwable {

    public InvalidPANException(String panNumber) {
        super("Invalid PAN number " + panNumber);
    }
}
