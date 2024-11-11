package ma.enset.comptecqrses.commonapi.exceptions;

public class BallanceInsufficientException extends RuntimeException {
    public BallanceInsufficientException(String message) {
        super(message);
    }
}
