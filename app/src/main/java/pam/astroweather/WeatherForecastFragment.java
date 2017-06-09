package pam.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherForecastFragment extends Fragment {

    private TextView[] date, temperature, description;

    public WeatherForecastFragment() {
        date = new TextView[10];
        temperature = new TextView[10];
        description = new TextView[10];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        date[0] = (TextView)v.findViewById(R.id.date_value_0);
        date[1] = (TextView)v.findViewById(R.id.date_value_1);
        date[2] = (TextView)v.findViewById(R.id.date_value_2);
        date[3] = (TextView)v.findViewById(R.id.date_value_3);
        date[4] = (TextView)v.findViewById(R.id.date_value_4);
        date[5] = (TextView)v.findViewById(R.id.date_value_5);
        date[6] = (TextView)v.findViewById(R.id.date_value_6);
        date[7] = (TextView)v.findViewById(R.id.date_value_7);
        date[8] = (TextView)v.findViewById(R.id.date_value_8);
        date[9] = (TextView)v.findViewById(R.id.date_value_9);
        temperature[0] = (TextView)v.findViewById(R.id.temperature_value_0);
        temperature[1] = (TextView)v.findViewById(R.id.temperature_value_1);
        temperature[2] = (TextView)v.findViewById(R.id.temperature_value_2);
        temperature[3] = (TextView)v.findViewById(R.id.temperature_value_3);
        temperature[4] = (TextView)v.findViewById(R.id.temperature_value_4);
        temperature[5] = (TextView)v.findViewById(R.id.temperature_value_5);
        temperature[6] = (TextView)v.findViewById(R.id.temperature_value_6);
        temperature[7] = (TextView)v.findViewById(R.id.temperature_value_7);
        temperature[8] = (TextView)v.findViewById(R.id.temperature_value_8);
        temperature[9] = (TextView)v.findViewById(R.id.temperature_value_9);
        description[0] = (TextView)v.findViewById(R.id.description_value_0);
        description[1] = (TextView)v.findViewById(R.id.description_value_1);
        description[2] = (TextView)v.findViewById(R.id.description_value_2);
        description[3] = (TextView)v.findViewById(R.id.description_value_3);
        description[4] = (TextView)v.findViewById(R.id.description_value_4);
        description[5] = (TextView)v.findViewById(R.id.description_value_5);
        description[6] = (TextView)v.findViewById(R.id.description_value_6);
        description[7] = (TextView)v.findViewById(R.id.description_value_7);
        description[8] = (TextView)v.findViewById(R.id.description_value_8);
        description[9] = (TextView)v.findViewById(R.id.description_value_9);
        return v;
    }

    public void update(String info){

    }
}
