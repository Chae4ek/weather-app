package chae4ek.weather.alert;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.UiThread;

public class AlertUtils {

  /**
   * Show the notification of msgId
   *
   * @deprecated only for tests
   */
  @Deprecated
  @UiThread
  public static void notify(final Context context, final String msg) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
  }

  /** Show the notification of msgId */
  @UiThread
  public static void notify(final Context context, final int msgId, final int duration) {
    Toast.makeText(context, msgId, duration).show();
  }

  /** Show the notification of msgId */
  @UiThread
  public static void notify(final Context context, final AlertException e) {
    notify(context, e.msgId, Toast.LENGTH_LONG);
    // TODO: log
    e.printStackTrace();
  }

  /**
   * Assert if obj is not null
   *
   * @throws AlertException with errorMsgId if obj is null
   */
  public static void assertNonNull(final Object obj, final int errorMsgId) throws AlertException {
    if (obj == null) throw new AlertException(errorMsgId);
  }
}
