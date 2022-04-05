package chae4ek.weather.parsers;

import android.graphics.Bitmap;
import androidx.annotation.WorkerThread;

public interface WeatherParser {

  enum DegreesType {
    CELSIUS,
    FAHRENHEIT,
    KELVIN
  }

  void setCityName(String cityName);

  String findDegrees(DegreesType degreesType);

  String findLocation();

  String findDescription();

  Bitmap loadIcon();

  @WorkerThread
  void waitForRequest();
}
