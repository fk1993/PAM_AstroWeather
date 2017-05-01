package pam.astroweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import java.util.*;

public class SettingsActivity extends AppCompatActivity {

    private static final List<Integer> FREQS = Arrays.asList(1, 5, 15, 30, 60);

    private EditText latitudeText, longitudeText;
    private Spinner latitudeSpinner, longitudeSpinner, freqSpinner;
    private Button saveButton, cancelButton;
    private double latitude, longitude;
    private int freq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findView();
        setSpinnerEntries(latitudeSpinner, R.array.latitude_array);
        setSpinnerEntries(longitudeSpinner, R.array.longitude_array);
        setSpinnerEntries(freqSpinner, R.array.freq_array);
        setFreqSpinnerSelectionListener();
        setButtons();
        setTextChangeListeners();
        update();
    }

    private void findView(){
        latitudeText = (EditText)findViewById(R.id.latitude);
        longitudeText = (EditText)findViewById(R.id.longitude);
        latitudeSpinner = (Spinner)findViewById(R.id.latitude_spinner);
        longitudeSpinner = (Spinner)findViewById(R.id.longitude_spinner);
        freqSpinner = (Spinner)findViewById(R.id.freq_spinner);
        saveButton = (Button)findViewById(R.id.save_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);
    }

    private void setSpinnerEntries(Spinner spinner, int id){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, id, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setFreqSpinnerSelectionListener(){
        freqSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                freq = FREQS.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setButtons(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitudeSpinner.getSelectedItemPosition() == 1)
                    latitude = -latitude;
                if (longitudeSpinner.getSelectedItemPosition() == 1)
                    longitude = -longitude;
                Intent data = new Intent();
                data.putExtra(MainActivity.LATITUDE, latitude);
                data.putExtra(MainActivity.LONGITUDE, longitude);
                data.putExtra(MainActivity.FREQ, freq);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setTextChangeListeners(){
        latitudeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                latitude = Double.parseDouble(latitudeText.getText().toString());
            }
        });
        longitudeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                longitude = Double.parseDouble(longitudeText.getText().toString());
            }
        });
    }

    private void update() {
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(MainActivity.LATITUDE, 0);
        longitude = intent.getDoubleExtra(MainActivity.LONGITUDE, 0);
        freq = intent.getIntExtra(MainActivity.FREQ, 15);
        latitudeSpinner.setSelection(latitude > 0 ? 0 : 1);
        longitudeSpinner.setSelection(longitude > 0 ? 0 : 1);
        freqSpinner.setSelection(FREQS.indexOf(freq));
        latitudeText.setText(Double.toString(Math.abs(latitude)));
        longitudeText.setText(Double.toString(Math.abs(longitude)));
    }
}
