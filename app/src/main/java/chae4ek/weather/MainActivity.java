package chae4ek.weather;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import chae4ek.weather.alert.AlertException;
import chae4ek.weather.alert.AlertUtils;

public class MainActivity extends AppCompatActivity {

    private final Weather weather = new Weather();

    private TextView textDegrees;
    private TextView textCity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textDegrees = findViewById(R.id.textDegrees);
        textCity = findViewById(R.id.textCity);

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
                            }
                        });

        // TODO: https://developer.android.com/training/appbar/actions#java
        // https://openweathermap.org/
    }

    private void updateWeather() {
        weather.waitForRequest();

        textDegrees.setText(weather.findDegrees(Weather.DegreesType.CELSIUS));
        textCity.setText(weather.findLocation());
    }
}
