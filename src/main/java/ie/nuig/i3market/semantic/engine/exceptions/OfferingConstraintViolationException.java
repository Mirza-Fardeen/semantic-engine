package ie.nuig.i3market.semantic.engine.exceptions;

public class OfferingConstraintViolationException extends RuntimeException {
    public OfferingConstraintViolationException() {
        super();
    }

    public OfferingConstraintViolationException(String message) {
        super(message);
    }

    public OfferingConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OfferingConstraintViolationException(Throwable cause) {
        super(cause);
    }
}
