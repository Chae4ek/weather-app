package chae4ek.weather.parsers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import chae4ek.weather.R;
import chae4ek.weather.alert.AlertException;
import chae4ek.weather.alert.AlertUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GoogleParser implements WeatherParser {

  private Element weather;
  private String cityRequest;

  @Override
  public void setCityName(final String cityName) {
    final String cityName0;
    if (cityName == null || "".equals(cityName0 = cityName.trim())) cityRequest = null;
    else cityRequest = cityName0.replace(' ', '+');
  }

  @Override
  public String findDegrees(final DegreesType degreesType) {
    AlertUtils.assertNonNull(degreesType, R.string.null_degreesType);
    AlertUtils.assertNonNull(weather, R.string.null_weather);
    final Element degrees;
    switch (degreesType) {
      case CELSIUS:
        degrees = weather.getElementById("wob_tm");
        AlertUtils.assertNonNull(degrees, R.string.null_degrees_C);
        return degrees.text() + " °C";
      case KELVIN:
        degrees = weather.getElementById("wob_tm"); // K = °C + 273
        AlertUtils.assertNonNull(degrees, R.string.null_degrees_K);
        return Integer.parseInt(degrees.text()) + 273 + " K";
      case FAHRENHEIT:
        degrees = weather.getElementById("wob_ttm");
        AlertUtils.assertNonNull(degrees, R.string.null_degrees_F);
        return degrees.text() + " °F";
    }
    return null;
  }

  @Override
  public String findLocation() {
    AlertUtils.assertNonNull(weather, R.string.null_weather);
    final Element location = weather.getElementById("wob_loc");
    AlertUtils.assertNonNull(location, R.string.null_location);
    return location.text();
  }

  @Override
  public String findDescription() {
    AlertUtils.assertNonNull(weather, R.string.null_weather);
    final Element description = weather.getElementById("wob_dc");
    AlertUtils.assertNonNull(description, R.string.null_description);
    return description.text();
  }

  @Override
  public Bitmap loadIcon() {
    AlertUtils.assertNonNull(weather, R.string.null_weather);
    final Element icon = weather.getElementById("wob_tci");
    AlertUtils.assertNonNull(icon, R.string.null_icon);
    String iconURL = icon.attr("src");
    AlertUtils.assertNonNull(iconURL, R.string.null_icon_url);
    final String[] urlParts = iconURL.split("/");
    iconURL = "https://ssl.gstatic.com/onebox/weather/256/" + urlParts[urlParts.length - 1];

    try (final InputStream in = new URL(iconURL).openStream()) {
      return BitmapFactory.decodeStream(in);
    } catch (final IOException e) {
      throw new AlertException(e, R.string.error_get_weather_icon);
    }
  }

  @Override
  public void waitForRequest() {
    weather = requireWeather();
  }

  private Element requireWeather() {
    try {
      String request = "https://www.google.com/search?q=weather+";
      if (cityRequest != null) request += cityRequest;
      final Document doc = Jsoup.connect(request).get();
      final Element weather = doc.getElementById("wob_wc");
      AlertUtils.assertNonNull(weather, R.string.error_get_weather);
      return weather;
    } catch (final IOException e) {
      throw new AlertException(e, R.string.error_get_html);
    }
  }
}
