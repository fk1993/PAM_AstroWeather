package pam.astroweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class SettingsActivity extends AppCompatActivity {

    private EditText latitudeText, longitudeText;
    private Spinner latitudeSpinner, longitudeSpinner, freqSpinner;
    private Button saveButton;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        latitudeText = (EditText)findViewById(R.id.latitude);
        longitudeText = (EditText)findViewById(R.id.longitude);
        latitudeSpinner = (Spinner)findViewById(R.id.latitude_spinner);
        longitudeSpinner = (Spinner)findViewById(R.id.longitude_spinner);
        freqSpinner = (Spinner)findViewById(R.id.freq_spinner);
        saveButton = (Button)findViewById(R.id.save_button);
        setSpinnerEntries(latitudeSpinner, R.array.latitude_array);
        setSpinnerEntries(longitudeSpinner, R.array.longitude_array);
        setSpinnerEntries(freqSpinner, R.array.freq_array);
        setSaveButton();
    }

    private void setSpinnerEntries(Spinner spinner, int id){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, id, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setSaveButton(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = Double.parseDouble(latitudeText.getText().toString());
                longitude = Double.parseDouble(longitudeText.getText().toString());
                finish();
            }
        });
    }
}
