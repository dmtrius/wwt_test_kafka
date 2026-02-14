package pl.wwt.auth.exception;

public class BadCommunicationException extends RuntimeException {
    public BadCommunicationException(String message) {
        super(message);
    }
}
