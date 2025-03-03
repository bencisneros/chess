package dataAccess;

public class NotEnoughInfo extends RuntimeException {
  public NotEnoughInfo(String message) {
    super(message);
  }
}
