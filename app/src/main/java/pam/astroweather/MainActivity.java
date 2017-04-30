package pam.astroweather;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;
import com.astrocalculator.*;

public class MainActivity extends AppCompatActivity {

    private Button settingsButton;
    private TextView timeText, locationText;
    private Fragment sunFragment, moonFragment;
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
    }

    private void getComponents(){
        settingsButton = (Button)findViewById(R.id.settings_button);
        timeText = (TextView)findViewById(R.id.time);
        locationText = (TextView)findViewById(R.id.location);

        FragmentManager manager = getSupportFragmentManager();
        sunFragment = manager.findFragmentById(R.id.sun_fragment);
        moonFragment = manager.findFragmentById(R.id.moon_fragment);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                Date t = calendar.getTime();
                timeText.setText(String.format("%tT", t));
            }
        });
    }

    private void updateInfo(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
