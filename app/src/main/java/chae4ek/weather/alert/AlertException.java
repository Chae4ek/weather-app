package chae4ek.weather.alert;

public class AlertException extends RuntimeException {

    public final int msgId;

    public AlertException(final int msgId) {
        this.msgId = msgId;
    }
}
