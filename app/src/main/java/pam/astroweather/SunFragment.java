package pam.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.astrocalculator.*;

public class SunFragment extends Fragment {

    private TextView riseTime, setTime, riseAzimuth, setAzimuth, civilMorningTime, civilEveningTime;

    public SunFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sun, container, false);
        riseTime = (TextView)v.findViewById(R.id.rise_time);
        riseAzimuth = (TextView)v.findViewById(R.id.rise_azimuth);
        setTime = (TextView)v.findViewById(R.id.set_time);
        setAzimuth = (TextView)v.findViewById(R.id.set_azimuth);
        civilMorningTime = (TextView)v.findViewById(R.id.civil_morning_time);
        civilEveningTime = (TextView)v.findViewById(R.id.civil_evening_time);
        return v;
    }

    public void update(AstroCalculator.SunInfo info){
        riseTime.setText(new AstroDateTimeFormatter(info.getSunrise()).getTime());
        setTime.setText(new AstroDateTimeFormatter(info.getSunset()).getTime());
        riseAzimuth.setText(String.format("%.2f", info.getAzimuthRise()));
        setAzimuth.setText(String.format("%.2f", info.getAzimuthSet()));
        civilMorningTime.setText(new AstroDateTimeFormatter(info.getTwilightMorning()).getTime());
        civilEveningTime.setText(new AstroDateTimeFormatter(info.getTwilightEvening()).getTime());
    }
}
