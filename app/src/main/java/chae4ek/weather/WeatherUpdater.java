package chae4ek.weather;

import android.graphics.Bitmap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import chae4ek.weather.alert.AlertUtils;
import chae4ek.weather.parsers.WeatherParser;
import chae4ek.weather.threads.AtomicTask;

public class WeatherUpdater extends AtomicTask<MainActivity> {

  private WeatherParser weather;

  public WeatherUpdater(final WeatherParser weather, final MainActivity activity) {
    super(activity);
    this.weather = weather;
  }

  /** Schedule to set the weather parser when this task is done */
  public void scheduleToSetWeatherParser(@NonNull final WeatherParser weatherParser) {
    if (isRunning.compareAndSet(false, true)) {
      weather = weatherParser;
      isRunning.set(false);
    } else {
      // TODO: schedule to set the weather when this task is done
      AlertUtils.notify(activity, "Wait until the weather update");
    }
  }

  @Override
  @UiThread
  public void preRun() {
    AlertUtils.notify(activity, R.string.refresh_start, Toast.LENGTH_SHORT);
  }

  @Override
  public void run() {
    weather.waitForRequest();
    final String[] degrees = weather.findDegrees();
    AlertUtils.assertNonNull(degrees, R.string.error404_degrees);
    final String location = weather.findLocation();
    AlertUtils.assertNonNull(location, R.string.error404_location);
    final String description = weather.findDescription();
    AlertUtils.assertNonNull(description, R.string.error404_description);
    final Bitmap icon = weather.loadIcon();
    AlertUtils.assertNonNull(icon, R.string.error404_icon);
    tryRunOnUi(
        () -> {
          activity.updateDegrees(degrees);
          activity.setTextCity(location);
          activity.setTextDescription(description);
          activity.setWeatherIcon(icon);
          AlertUtils.notify(activity, R.string.refresh_end, Toast.LENGTH_SHORT);
        });
  }
}
