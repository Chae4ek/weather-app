package chae4ek.weather;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import chae4ek.weather.alert.AlertException;
import chae4ek.weather.alert.AlertUtils;

public class MainActivity extends AppCompatActivity {

    private final Weather weather = new Weather();

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

        // TODO: remove
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        final TextInputEditText inputCity = findViewById(R.id.inputCity);
        findViewById(R.id.btnRefresh)
                .setOnClickListener(
                        view -> {
                            final Editable city = inputCity.getText();
                            weather.setCityName(city == null ? null : city.toString());
                            try {
                                updateWeather();
                            } catch (final AlertException e) {
                                AlertUtils.notify(this, e.msgId);
                                e.printStackTrace();
                            }
                        });

        // TODO: https://developer.android.com/training/appbar/actions#java
        // TODO: https://stackoverflow.com/questions/3400028/close-virtual-keyboard-on-button-press
        // TODO: parse:
        // https://search.yahoo.com/search?p=weather+
        // https://www.bing.com/search?q=weather+
        // https://duckduckgo.com/?q=weather+
    }

    private void updateWeather() {
        weather.waitForRequest();

        // TODO: check weather.? for null
        textDegrees.setText(weather.findDegrees(Weather.DegreesType.CELSIUS));
        textCity.setText(weather.findLocation());
        weatherIcon.setImageBitmap(weather.loadIcon());
        textDescription.setText(weather.findDescription());
    }
}
