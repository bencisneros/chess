package DataAccess;

public class Unauthorized extends RuntimeException {
    public Unauthorized(String message) {
        super(message);
    }
}
