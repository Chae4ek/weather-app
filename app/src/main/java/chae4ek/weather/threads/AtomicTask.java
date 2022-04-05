package chae4ek.weather.threads;

import android.app.Activity;
import androidx.annotation.WorkerThread;
import chae4ek.weather.alert.AlertException;
import chae4ek.weather.alert.AlertUtils;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AtomicTask<A extends Activity> {

  private final UncaughtExceptionHandler handler;
  protected final AtomicBoolean isRunning = new AtomicBoolean(false);
  protected final A activity;

  public AtomicTask(final A activity) {
    this.activity = activity;
    handler =
        (t, e) -> {
          if (e instanceof AlertException) {
            activity.runOnUiThread(() -> AlertUtils.notify(activity, (AlertException) e));
          }
        };
  }

  /** Run the task in another thread if it is not running */
  public final void compareAndStart() {
    if (isRunning.compareAndSet(false, true)) {
      final Thread t =
          new Thread(
              () -> {
                run();
                isRunning.set(false);
              });
      t.setUncaughtExceptionHandler(handler);
      t.start();
    }
  }

  @WorkerThread
  public abstract void run();

  @WorkerThread
  public void tryRunOnUi(final Runnable task) {
    activity.runOnUiThread(
        () -> {
          try {
            task.run();
          } catch (final AlertException e) {
            AlertUtils.notify(activity, e);
          }
        });
  }
}
