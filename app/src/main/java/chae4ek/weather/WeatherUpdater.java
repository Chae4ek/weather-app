package chae4ek.weather;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
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
  public void scheduleToSetWeather(@NonNull final WeatherParser weather) {
    if (isRunning.compareAndSet(false, true)) {
      this.weather = weather;
      isRunning.set(false);
    } else {
      // TODO: schedule to set the weather when this task is done
      AlertUtils.notify(activity, "Wait until the weather update");
    }
  }

  @Override
  public void run() {
    weather.waitForRequest();
    final String degrees = weather.findDegrees(WeatherParser.DegreesType.CELSIUS);
    final String location = weather.findLocation();
    final String description = weather.findDescription();
    final Bitmap icon = weather.loadIcon();
    tryRunOnUi(
        () -> {
          AlertUtils.assertNonNull(degrees, R.string.error404_degrees);
          AlertUtils.assertNonNull(location, R.string.error404_location);
          AlertUtils.assertNonNull(description, R.string.error404_description);
          AlertUtils.assertNonNull(icon, R.string.error404_icon);
          activity.setTextDegrees(degrees);
          activity.setTextCity(location);
          activity.setTextDescription(description);
          activity.setWeatherIcon(icon);
        });
  }
}
