package pam.astroweather;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;

public class BasicInfoFragment extends Fragment {

    private TextView locationName, locationCoord, time, temperature, pressure, description;
    private double latitude, longitude;
    private MainActivity activity;

    public BasicInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        activity = (MainActivity) context;
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
        description = (TextView)v.findViewById(R.id.description_value);
        update(activity.getLocationName(), activity.getWeatherInfo());
        activity.updateLocation();
        return v;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void update(String locationName, String info){
        this.locationName.setText(locationName);
        try {
            JSONObject query = new JSONObject(info).getJSONObject("query");
            if (query.getInt("count") > 0){
                JSONObject results = query.getJSONObject("results").getJSONObject("channel");
                time.setText(results.getString("lastBuildDate"));
                String latitude = results.getJSONObject("item").getString("lat");
                String longitude = results.getJSONObject("item").getString("long");
                updateCoord(latitude, longitude);
                updateConditions(results);
                description.setText(results.getJSONObject("item").getJSONObject("condition").getString("text"));
            }
        } catch(JSONException e){
            Toast.makeText(activity, R.string.format_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCoord(String latitudeString, String longitudeString){
        latitude = Double.parseDouble(latitudeString);
        longitude = Double.parseDouble(longitudeString);
        String latitudeDirection = latitude > 0 ? "N" : "S";
        String longitudeDirection = longitude > 0 ? "E" : "W";
        locationCoord.setText(Math.abs(latitude) + " " + latitudeDirection + " " + Math.abs(longitude) + " " + longitudeDirection);
    }

    private void updateConditions(JSONObject results){
        try {
            JSONObject units = results.getJSONObject("units");
            String temperatureUnit = units.getString("temperature");
            String pressureUnit = units.getString("pressure");
            String temperature = results.getJSONObject("item").getJSONObject("condition").getString("temp");
            String pressure = results.getJSONObject("atmosphere").getString("pressure");
            this.temperature.setText(temperature + " " + temperatureUnit);
            this.pressure.setText(pressure + " " + pressureUnit);
        } catch(JSONException e){
            Toast.makeText(activity, R.string.format_error, Toast.LENGTH_SHORT).show();
        }
    }
}
