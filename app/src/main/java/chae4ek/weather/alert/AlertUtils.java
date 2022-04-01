package chae4ek.weather.alert;

import android.content.Context;
import android.widget.Toast;

public class AlertUtils {

    /** Show the notification of msgId */
    public static void notify(final Context context, final int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
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
