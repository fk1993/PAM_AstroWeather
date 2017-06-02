package pam.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;

public class AdditionalInfoFragment extends Fragment {

    private TextView windForce, windDirection, humidity, visibility;

    public AdditionalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_additional_info, container, false);
        windForce = (TextView)v.findViewById(R.id.wind_force_value);
        windDirection = (TextView)v.findViewById(R.id.wind_direction_value);
        humidity = (TextView)v.findViewById(R.id.humidity_value);
        visibility = (TextView)v.findViewById(R.id.visibility_value);
        return v;
    }

    public void update(File infoFile){

    }
}
