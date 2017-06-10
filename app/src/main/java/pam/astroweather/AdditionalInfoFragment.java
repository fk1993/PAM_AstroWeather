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

public class AdditionalInfoFragment extends Fragment {

    private TextView windForce, windDirection, humidity, visibility;
    private MainActivity activity;

    public AdditionalInfoFragment() {
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
        View v = inflater.inflate(R.layout.fragment_additional_info, container, false);
        windForce = (TextView)v.findViewById(R.id.wind_force_value);
        windDirection = (TextView)v.findViewById(R.id.wind_direction_value);
        humidity = (TextView)v.findViewById(R.id.humidity_value);
        visibility = (TextView)v.findViewById(R.id.visibility_value);
        update(activity.getWeatherInfo());
        return v;
    }

    public void update(String info){
        try {
            JSONObject query = new JSONObject(info).getJSONObject("query");
            if (query.getInt("count") > 0) {
                JSONObject results = query.getJSONObject("results").getJSONObject("channel");
                JSONObject units = results.getJSONObject("units");
                String distanceUnit = units.getString("distance");
                String speedUnit = units.getString("speed");
                JSONObject wind = results.getJSONObject("wind");
                windForce.setText(wind.getString("speed") + " " + speedUnit);
                windDirection.setText(wind.getString("direction"));
                JSONObject atmosphere = results.getJSONObject("atmosphere");
                humidity.setText(atmosphere.getString("humidity") + " %");
                visibility.setText(atmosphere.getString("visibility") + " " + distanceUnit);
            }
        } catch(JSONException e){
            Toast.makeText(activity, R.string.format_error, Toast.LENGTH_SHORT).show();
        }
    }
}
