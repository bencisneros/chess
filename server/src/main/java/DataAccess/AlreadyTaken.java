package DataAccess;

public class AlreadyTaken extends RuntimeException {
    public AlreadyTaken(String message) {
        super(message);
    }
}
