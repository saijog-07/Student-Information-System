package exceptions;

public class InvalidCourseDataException extends RuntimeException {
    public InvalidCourseDataException(String message) {
        super(message);
    }
}
