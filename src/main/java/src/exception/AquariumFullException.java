package src.exception;

public class AquariumFullException  extends RuntimeException {
    public AquariumFullException(String message) {
        super(message);
    }
}