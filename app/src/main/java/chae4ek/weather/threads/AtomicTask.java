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
          isRunning.set(false);
          if (e instanceof AlertException) {
            activity.runOnUiThread(() -> AlertUtils.notify(activity, (AlertException) e));
          }
        };
  }

  /**
   * Run the task in another thread if it is not running
   *
   * @return true if the task is successfully started
   */
  public final boolean compareAndStart() {
    if (!isRunning.compareAndSet(false, true)) return false;
    preRun();
    final Thread t =
        new Thread(
            () -> {
              run();
              isRunning.set(false);
            });
    t.setUncaughtExceptionHandler(handler);
    t.start();
    return true;
  }

  public void preRun() {}

  @WorkerThread
  public abstract void run();

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
