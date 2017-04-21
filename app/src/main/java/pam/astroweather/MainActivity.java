package pam.astroweather;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;
import static java.util.Calendar.*;

public class MainActivity extends AppCompatActivity
    implements SunFragment.OnFragmentInteractionListener, MoonFragment.OnFragmentInteractionListener {

    private Button settingsButton;
    private TextView time, location;
    private Fragment sunFragment, moonFragment;
    private final Timer clock = new Timer(), infoUpdateTimer = new Timer();
    private final Calendar calendar = Calendar.getInstance();
    private int minutes = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponents();
        setComponents();

        clock.schedule(new TimerTask(){
            @Override
            public void run(){
                int h = calendar.get(HOUR_OF_DAY);
                int m = calendar.get(MINUTE);
                int s = calendar.get(SECOND);
                time.setText(h + ":" + m + ":" + s);
            }
        }, 0, 1000);

        infoUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
            }
        }, 0, minutes * 60000);
    }

    private void getComponents(){
        settingsButton = (Button)findViewById(R.id.settings_button);
        time = (TextView)findViewById(R.id.time);
        location = (TextView)findViewById(R.id.location);

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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
