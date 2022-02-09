package ie.nuig.i3market.semantic.engine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidInputException extends ResponseStatusException {

    public InvalidInputException(HttpStatus status) {
        super(status);
    }

    public InvalidInputException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public InvalidInputException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public InvalidInputException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return super.getStatus();
    }
}
