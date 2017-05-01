package pam.astroweather;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import java.util.*;
import static java.util.Calendar.*;
import com.astrocalculator.*;

public class MainActivity extends AppCompatActivity {

    public static final String LATITUDE = "pam.astroweather.latitude";
    public static final String LONGITUDE = "pam.astroweather.longitude";
    public static final String FREQ = "pam.astroweather.freq";
    private static final int REQUEST_CODE_SETTINGS = 1;

    private TextView timeText, locationText;
    private SunFragment sunFragment;
    private MoonFragment moonFragment;
    private Button settingsButton;
    private final Timer clock = new Timer(), infoUpdateTimer = new Timer();
    private AstroCalculator.Location location = new AstroCalculator.Location(51, 19);
    private int freq = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponents();
        setComponents();
        if (savedInstanceState != null){
            location.setLatitude(savedInstanceState.getDouble(LATITUDE));
            location.setLongitude(savedInstanceState.getDouble(LONGITUDE));
            freq = savedInstanceState.getInt(FREQ);
        }
        updateLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putDouble(LATITUDE, location.getLatitude());
        state.putDouble(LONGITUDE, location.getLongitude());
        state.putInt(FREQ, freq);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK){
            location.setLatitude(data.getDoubleExtra(LATITUDE, location.getLatitude()));
            location.setLongitude(data.getDoubleExtra(LONGITUDE, location.getLongitude()));
            freq = data.getIntExtra(FREQ, 15);
            updateLocation();
            updateInfo();
            setInfoUpdateTimer(freq);
        }
    }

    private void getComponents(){
        settingsButton = (Button)findViewById(R.id.settings_button);
        timeText = (TextView)findViewById(R.id.time);
        locationText = (TextView)findViewById(R.id.location);

        FragmentManager manager = getSupportFragmentManager();
        sunFragment = (SunFragment)manager.findFragmentById(R.id.sun_fragment);
        moonFragment = (MoonFragment)manager.findFragmentById(R.id.moon_fragment);
    }

    private void setComponents(){
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra(LATITUDE, location.getLatitude());
                intent.putExtra(LONGITUDE, location.getLongitude());
                intent.putExtra(FREQ, freq);
                startActivityForResult(intent, REQUEST_CODE_SETTINGS);
            }
        });

        clock.schedule(new TimerTask(){
            @Override
            public void run(){
                updateClock();
            }
        }, 0, 1000);

        setInfoUpdateTimer(15);
    }

    private void setInfoUpdateTimer(int minutes){
        infoUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateInfo();
            }
        }, 0, minutes * 60000);
    }

    private void updateClock(){
        Calendar calendar = Calendar.getInstance();
        final Date t = calendar.getTime();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeText.setText(String.format("%tT", t));
            }
        });
    }

    private void updateInfo(){
        Calendar calendar = Calendar.getInstance();
        AstroDateTime time = new AstroDateTime(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH),
                calendar.get(HOUR_OF_DAY), calendar.get(MINUTE), calendar.get(SECOND), 0, true);
        AstroCalculator calculator = new AstroCalculator(time, location);
        final AstroCalculator.SunInfo sunInfo = calculator.getSunInfo();
        final AstroCalculator.MoonInfo moonInfo = calculator.getMoonInfo();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sunFragment.update(sunInfo);
                moonFragment.update(moonInfo);
            }
        });
    }

    private void updateLocation(){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String latitudeDirection = latitude > 0 ? "N" : "S";
        String longitudeDirection = longitude > 0 ? "E" : "W";
        locationText.setText(Math.abs(latitude) + " " + latitudeDirection + " " + Math.abs(longitude) + " " + longitudeDirection);
    }
}
