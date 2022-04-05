package chae4ek.weather;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import chae4ek.weather.parsers.GoogleParser;
import chae4ek.weather.parsers.WeatherParser;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

  private final WeatherParser weather = new GoogleParser();
  private final WeatherUpdater weatherUpdater = new WeatherUpdater(weather, this);

  private TextView textDegrees;
  private TextView textCity;
  private ImageView weatherIcon;
  private TextView textDescription;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textDegrees = findViewById(R.id.textDegrees);
    textCity = findViewById(R.id.textCity);
    weatherIcon = findViewById(R.id.weatherIcon);
    textDescription = findViewById(R.id.textDescription);

    final TextInputEditText inputCity = findViewById(R.id.inputCity);
    findViewById(R.id.btnRefresh)
        .setOnClickListener(
            view -> {
              final Editable city = inputCity.getText();
              weather.setCityName(city == null ? null : city.toString());
              weatherUpdater.compareAndStart();
            });

    // TODO: https://developer.android.com/training/appbar/actions#java
    // TODO: https://stackoverflow.com/questions/3400028/close-virtual-keyboard-on-button-press
    // TODO: parse:
    // https://search.yahoo.com/search?p=weather+
    // https://www.bing.com/search?q=weather+
    // https://duckduckgo.com/?q=weather+
  }

  @UiThread
  public void setTextDegrees(final String degrees) {
    textDegrees.setText(degrees);
  }

  @UiThread
  public void setTextCity(final String location) {
    textCity.setText(location);
  }

  @UiThread
  public void setTextDescription(final String description) {
    textDescription.setText(description);
  }

  @UiThread
  public void setWeatherIcon(final Bitmap icon) {
    weatherIcon.setImageBitmap(icon);
  }
}
