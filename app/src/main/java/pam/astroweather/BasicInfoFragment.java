package pam.astroweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;

import java.io.InputStream;
import java.net.URL;

public class BasicInfoFragment extends Fragment {

    private TextView locationName, locationCoord, time, temperature, pressure, description;
    private ImageView image;
    private double latitude, longitude;
    private MainActivity activity;
    boolean isViewCreated = false;

    private AsyncTask<String, Void, Bitmap> imageDownloadTask = new AsyncTask<String, Void, Bitmap>(){
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            try {
                InputStream input = new URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                return bitmap;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            image.setImageBitmap(bitmap);
        }
    };

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
        image = (ImageView)v.findViewById(R.id.image);
        isViewCreated = true;
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
        try {
            JSONObject query = new JSONObject(info).getJSONObject("query");
            this.locationName.setText(locationName);
            if (query.getInt("count") > 0){
                JSONObject results = query.getJSONObject("results").getJSONObject("channel");
                time.setText(results.getString("lastBuildDate"));
                String latitude = results.getJSONObject("item").getString("lat");
                String longitude = results.getJSONObject("item").getString("long");
                updateCoord(latitude, longitude);
                updateConditions(results);
                description.setText(results.getJSONObject("item").getJSONObject("condition").getString("text"));
                updateImage(results);
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

    private void updateImage(JSONObject results){
        try {
            String description = results.getJSONObject("item").getString("description");
            String url = description.split("\"")[1];
            imageDownloadTask.execute(url);
        } catch (JSONException e) {
            Toast.makeText(activity, R.string.format_error, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
