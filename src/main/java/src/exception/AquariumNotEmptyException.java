package src.exception;

public class AquariumNotEmptyException  extends RuntimeException {
    public AquariumNotEmptyException(String message) {
        super(message);
    }
}