package pam.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MoonFragment extends Fragment {

    private TextView riseTime, setTime, newMoonDate, fullMoonDate, moonIllumination, moonAge;

    public MoonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_moon, container, false);
        riseTime = (TextView)v.findViewById(R.id.rise_time);
        setTime = (TextView)v.findViewById(R.id.set_time);
        newMoonDate = (TextView)v.findViewById(R.id.new_moon_date);
        fullMoonDate = (TextView)v.findViewById(R.id.full_moon_date);
        moonIllumination = (TextView)v.findViewById(R.id.moon_illumination_value);
        moonAge = (TextView)v.findViewById(R.id.moon_age_value);
        return v;
    }
}
