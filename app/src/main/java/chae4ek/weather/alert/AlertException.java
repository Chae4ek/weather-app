package chae4ek.weather.alert;

public class AlertException extends RuntimeException {

  public final int msgId;

  public AlertException(final int msgId) {
    this.msgId = msgId;
  }

  public AlertException(final Throwable cause, final int msgId) {
    super(cause);
    this.msgId = msgId;
  }
}
