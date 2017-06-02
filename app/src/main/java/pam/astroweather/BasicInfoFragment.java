package pam.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;

public class BasicInfoFragment extends Fragment {

    private TextView locationName, locationCoord, time, temperature, pressure;
    private double latitude, longitude;

    public BasicInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_basic_info, container, false);
        locationName = (TextView)v.findViewById(R.id.location_name);
        locationCoord = (TextView)v.findViewById(R.id.location_coord);
        time = (TextView)v.findViewById(R.id.time_value);
        temperature = (TextView)v.findViewById(R.id.temperature_value);
        pressure = (TextView)v.findViewById(R.id.pressure_value);
        return v;
    }

    public void update(File infoFile){

    }
}
