package exceptions;

public class InvalidTeacherDataException extends RuntimeException {
    public InvalidTeacherDataException(String message) {
        super(message);
    }
}
