package chae4ek.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import chae4ek.weather.alert.AlertUtils;
import chae4ek.weather.parsers.GoogleParser;
import chae4ek.weather.parsers.WeatherParser;
import chae4ek.weather.parsers.WeatherParser.DegreesType;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

  private final WeatherParser weather = new GoogleParser();
  private final WeatherUpdater weatherUpdater = new WeatherUpdater(weather, this);

  private TextInputEditText inputCity;

  private TextView textDegreesType;
  private TextView textDegrees;
  private TextView textCity;
  private ImageView weatherIcon;
  private TextView textDescription;

  private SharedPreferences.Editor prefs;
  private boolean isDarkTheme;
  private static final String[] degreesNull = new String[DegreesType.values().length];
  private String[] degrees = degreesNull;
  private int selectedDegreesType;

  static {
    for (int i = 0; i < DegreesType.values().length; ++i) degreesNull[i] = "--";
  }

  private InputMethodManager imm;

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.action_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
    if (item.getItemId() == R.id.btnSwitchTheme) {
      isDarkTheme = !isDarkTheme;
      prefs.putBoolean("NightMode", isDarkTheme);
      prefs.apply();
      AppCompatDelegate.setDefaultNightMode(
          isDarkTheme ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onSaveInstanceState(@NonNull final Bundle outState) {
    outState.putInt("degreesType", selectedDegreesType);
    outState.putStringArray("degrees", degrees == degreesNull ? null : degrees);
    outState.putCharSequence("city", textCity.getText());
    outState.putCharSequence("description", textDescription.getText());
    final BitmapDrawable iconDrawable = (BitmapDrawable) weatherIcon.getDrawable();
    if (iconDrawable != null) {
      final Bitmap icon = iconDrawable.getBitmap();
      outState.putParcelable("icon", icon);
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    selectedDegreesType = savedInstanceState.getInt("degreesType");
    updateDegrees(savedInstanceState.getStringArray("degrees"));
    textCity.setText(savedInstanceState.getCharSequence("city"));
    textDescription.setText(savedInstanceState.getCharSequence("description"));
    weatherIcon.setImageBitmap(savedInstanceState.getParcelable("icon"));
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final SharedPreferences prefs = getSharedPreferences("AppSettingPrefs", Context.MODE_PRIVATE);
    this.prefs = prefs.edit();

    isDarkTheme = prefs.getBoolean("NightMode", false);
    AppCompatDelegate.setDefaultNightMode(
        isDarkTheme ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

    textDegreesType = findViewById(R.id.textDegreesType);
    textDegrees = findViewById(R.id.textDegrees);
    textCity = findViewById(R.id.textCity);
    weatherIcon = findViewById(R.id.weatherIcon);
    textDescription = findViewById(R.id.textDescription);
    textCity.setText(null);
    textDescription.setText(null);

    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputCity = findViewById(R.id.inputCity);

    inputCity.setOnEditorActionListener(
        (v, actionId, event) -> {
          if (actionId == EditorInfo.IME_ACTION_DONE) {
            refresh();
            return true;
          }
          return false;
        });

    findViewById(R.id.btnRefresh).setOnClickListener(view -> refresh());

    findViewById(R.id.btnDegreesType)
        .setOnClickListener(
            view -> {
              if (++selectedDegreesType == DegreesType.values().length) selectedDegreesType = 0;
              updateDegrees(degrees);
            });

    // TODO: parse:
    // https://search.yahoo.com/search?p=weather+
    // https://www.bing.com/search?q=weather+
    // https://duckduckgo.com/?q=weather+
  }

  private void refresh() {
    imm.hideSoftInputFromWindow(
        inputCity.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

    final Editable city = inputCity.getText();
    weather.setCityName(city == null ? null : city.toString());
    if (!weatherUpdater.compareAndStart()) {
      // TODO: replace with a graphical update
      AlertUtils.notify(this, R.string.error_refresh, Toast.LENGTH_SHORT);
    }
  }

  @UiThread
  public void updateDegrees(@Nullable final String[] degrees) {
    this.degrees = degrees == null ? degreesNull : degrees;
    textDegreesType.setText(DegreesType.values()[selectedDegreesType].name);
    textDegrees.setText(this.degrees[selectedDegreesType]);
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
