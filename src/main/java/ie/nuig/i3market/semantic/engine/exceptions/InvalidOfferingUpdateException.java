package ie.nuig.i3market.semantic.engine.exceptions;

public class InvalidOfferingUpdateException extends RuntimeException {

    public InvalidOfferingUpdateException(String message) {
        super(message);
    }

    public InvalidOfferingUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOfferingUpdateException(Throwable cause) {
        super(cause);
    }

    protected InvalidOfferingUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
