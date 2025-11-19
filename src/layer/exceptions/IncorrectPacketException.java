package layer.exceptions;

public class IncorrectPacketException extends RuntimeException {
    public IncorrectPacketException(String message) {
        super(message);
    }
}
