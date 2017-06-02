package pam.astroweather;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.util.*;
import static java.util.Calendar.*;
import com.astrocalculator.*;

public class MainActivity extends AppCompatActivity {

    public static final String LATITUDE = "pam.astroweather.latitude";
    public static final String LONGITUDE = "pam.astroweather.longitude";
    public static final String FREQ = "pam.astroweather.freq";
    public static final int REQUEST_CODE_SETTINGS = 1;

    private TextView timeText, locationText;
    private SunFragment sunFragment;
    private MoonFragment moonFragment;
    private BasicInfoFragment basicInfoFragment;
    private AdditionalInfoFragment additionalInfoFragment;
    private WeatherForecastFragment weatherForecastFragment;
    private ViewPager fragmentPager;
    private Button settingsButton, updateButton;
    private final Timer clock = new Timer();
    private Timer infoUpdateTimer;
    private AstroCalculator.Location location = new AstroCalculator.Location(51, 19);
    private int freq = 15;
    private String locationName = "lodz, pl", units = "c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setComponents();
        if (savedInstanceState != null){
            location.setLatitude(savedInstanceState.getDouble(LATITUDE));
            location.setLongitude(savedInstanceState.getDouble(LONGITUDE));
            freq = savedInstanceState.getInt(FREQ);
        }
        updateLocation();
        setTimers();
    }

    @Override
    protected void onSaveInstanceState(Bundle state){
        state.putDouble(LATITUDE, location.getLatitude());
        state.putDouble(LONGITUDE, location.getLongitude());
        state.putInt(FREQ, freq);
        infoUpdateTimer.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK){
            location.setLatitude(data.getDoubleExtra(LATITUDE, location.getLatitude()));
            location.setLongitude(data.getDoubleExtra(LONGITUDE, location.getLongitude()));
            freq = data.getIntExtra(FREQ, 15);
            updateLocation();
            infoUpdateTimer.cancel();
            setInfoUpdateTimer(freq);
        }
    }

    private void setComponents(){
        settingsButton = (Button)findViewById(R.id.settings_button);
        updateButton = (Button)findViewById(R.id.update_button);
        timeText = (TextView)findViewById(R.id.time);
        locationText = (TextView)findViewById(R.id.location);
        initFragments();
        fragmentPager = (ViewPager)findViewById(R.id.fragment_pager);
        LinearLayout fragmentLinearLayout = (LinearLayout)findViewById(R.id.fragment_linear_layout);

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
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    File infoFile = getWeatherInfo();
                    basicInfoFragment.update(infoFile);
                    additionalInfoFragment.update(infoFile);
                } else
                    Toast.makeText(MainActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
            }
        });
        if (fragmentPager != null)
            setPager();
        else if (fragmentLinearLayout != null)
            setLinearLayout();
    }

    private void initFragments() {
        sunFragment = new SunFragment();
        moonFragment = new MoonFragment();
        basicInfoFragment = new BasicInfoFragment();
        additionalInfoFragment = new AdditionalInfoFragment();
        weatherForecastFragment = new WeatherForecastFragment();
    }

    private void setPager() {
        fragmentPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            Fragment[] fragments = { sunFragment, moonFragment, basicInfoFragment,
                    additionalInfoFragment, weatherForecastFragment };

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        fragmentPager.setCurrentItem(0);
        fragmentPager.setOffscreenPageLimit(4);
    }

    private  void setLinearLayout(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.astro_fragment_container, sunFragment)
                .add(R.id.astro_fragment_container, moonFragment)
                .add(R.id.weather_fragment_container, basicInfoFragment)
                .add(R.id.weather_fragment_container, additionalInfoFragment)
                .add(R.id.weather_forecast_fragment_container, weatherForecastFragment)
                .commit();
    }

    private void setTimers(){
        clock.schedule(new TimerTask(){
            @Override
            public void run(){
                updateClock();
            }
        }, 0, 1000);
        setInfoUpdateTimer(freq);
    }

    private void setInfoUpdateTimer(int minutes){
        infoUpdateTimer = new Timer();
        infoUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateInfo();
            }
        }, 500, minutes * 60000);
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

    private File getWeatherInfo(){
        try {
            DownloadTask task = new DownloadTask(this);
            task.execute(locationName, units);
            File file = task.get();
            return file;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private boolean checkConnection(){
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
        if (networkInfo == null)
            return false;
        else
            return networkInfo.isConnected();
    }
}
