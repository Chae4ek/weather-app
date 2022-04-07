package chae4ek.weather.parsers;

import android.graphics.Bitmap;
import androidx.annotation.WorkerThread;

public interface WeatherParser {

  enum DegreesType {
    CELSIUS("°C"),
    FAHRENHEIT("°F"),
    KELVIN("K");

    public final String name;

    DegreesType(final String name) {
      this.name = name;
    }
  }

  void setCityName(String cityName);

  /**
   * @return degrees ordered by {@link DegreesType#ordinal}
   */
  String[] findDegrees();

  String findLocation();

  String findDescription();

  Bitmap loadIcon();

  @WorkerThread
  void waitForRequest();
}
