package pam.astroweather;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    Button settingsButton;
    TextView time, location;
    Fragment sunFragment, moonFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponenets();
        setComponenets();
    }

    private void getComponenets(){
        settingsButton = (Button)findViewById(R.id.settings_button);
        time = (TextView)findViewById(R.id.time);
        location = (TextView)findViewById(R.id.location);

        FragmentManager manager = getSupportFragmentManager();
        sunFragment = manager.findFragmentById(R.id.sun_fragment);
        moonFragment = manager.findFragmentById(R.id.moon_fragment);
    }

    private void setComponenets(){
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
