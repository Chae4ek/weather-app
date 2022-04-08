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

  /**
   * @return matrix 8x3, where the rows are temperature for every 3 hours (°C, °F, K) starting at
   *     the moment
   */
  String[][] findTemperature();

  String findLocation();

  String findDescription();

  Bitmap loadIcon();

  @WorkerThread
  void waitForRequest();
}
