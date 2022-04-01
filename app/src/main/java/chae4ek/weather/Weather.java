package chae4ek.weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import chae4ek.weather.alert.AlertException;
import chae4ek.weather.alert.AlertUtils;

public class Weather {

    public enum DegreesType {
        CELSIUS,
        FAHRENHEIT,
        KELVIN
    }

    private /*volatile*/ Element weather;
    private String cityRequest;

    public void setCityName(final String cityName) {
        final String cityName0;
        if (cityName == null || "".equals(cityName0 = cityName.trim())) cityRequest = null;
        else cityRequest = '+' + cityName0.replace(' ', '+');
    }

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

    public String findLocation() {
        AlertUtils.assertNonNull(weather, R.string.null_weather);
        final Element location = weather.getElementById("wob_loc");
        AlertUtils.assertNonNull(location, R.string.null_location);
        return location.text();
    }

    /**
     * @deprecated TODO: make in a different thread
     */
    @Deprecated
    public void waitForRequest() {
        weather = requireWeather();
    }

    private Element requireWeather() {
        try {
            String request = "https://www.google.com/search?q=weather";
            if (cityRequest != null) request += cityRequest;
            final Document doc = Jsoup.connect(request).get();
            final Element weather = doc.getElementById("wob_wc");
            AlertUtils.assertNonNull(weather, R.string.error_get_weather);
            return weather;
        } catch (final IOException e) {
            throw new AlertException(R.string.error_get_html);
        }
    }
}
