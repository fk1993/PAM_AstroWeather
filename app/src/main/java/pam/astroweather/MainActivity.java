package pam.astroweather;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;
import static java.util.Calendar.*;
import com.astrocalculator.*;

public class MainActivity extends AppCompatActivity {

    private Button settingsButton;
    private TextView timeText, locationText;
    private SunFragment sunFragment;
    private MoonFragment moonFragment;
    private AstroCalculator calculator;
    private AstroCalculator.SunInfo sunInfo;
    private AstroCalculator.MoonInfo moonInfo;
    private AstroCalculator.Location location;
    private AstroDateTime time;
    private final Timer clock = new Timer(), infoUpdateTimer = new Timer();
    private int minutes = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponents();
        setComponents();
        location = new AstroCalculator.Location(0, 0);
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
            }
        });

        clock.schedule(new TimerTask(){
            @Override
            public void run(){
                updateClock();
            }
        }, 0, 1000);

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
        time = new AstroDateTime(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH),
                calendar.get(HOUR_OF_DAY), calendar.get(MINUTE), calendar.get(SECOND), 0, false);
        calculator = new AstroCalculator(time, location);
        sunInfo = calculator.getSunInfo();
        moonInfo = calculator.getMoonInfo();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sunFragment.update(sunInfo);
                moonFragment.update(moonInfo);
            }
        });
    }
}
