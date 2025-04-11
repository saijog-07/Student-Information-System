package exceptions;

public class InvalidEnrollmentDataException extends RuntimeException {
    public InvalidEnrollmentDataException(String message) {
        super(message);
    }
}
