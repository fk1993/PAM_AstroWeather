package pam.astroweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import android.util.Xml;
import java.util.*;
import java.io.*;
import org.xmlpull.v1.*;
import static org.xmlpull.v1.XmlPullParser.*;

public class SettingsActivity extends AppCompatActivity {

    private static final List<Integer> FREQS = Arrays.asList(1, 5, 15, 30, 60);

    private List<String> locations;
    private EditText locationText;
    private Spinner locationSpinner, unitsSpinner, freqSpinner;
    private Button saveButton, cancelButton, addLocationButton;
    private String locationName, units;
    private int freq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (locations == null)
            locations = createList();
        findView();
        setSpinnerEntries(unitsSpinner, R.array.units);
        setSpinnerEntries(freqSpinner, R.array.freq_array);
        setLocationSpinnerEntries();
        setLocationSpinnerSelectionListener();
        setUnitsSpinnerSelectionListener();
        setFreqSpinnerSelectionListener();
        setButtons();
        update();
    }

    private void findView(){
        locationSpinner = (Spinner)findViewById(R.id.location_spinner);
        unitsSpinner = (Spinner)findViewById(R.id.units_spinner);
        locationText = (EditText)findViewById(R.id.location_text);
        addLocationButton = (Button)findViewById(R.id.add_location_button);
        freqSpinner = (Spinner)findViewById(R.id.freq_spinner);
        saveButton = (Button)findViewById(R.id.save_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);
    }

    private void setSpinnerEntries(Spinner spinner, int id){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, id, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setLocationSpinnerEntries(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
    }

    private void setLocationSpinnerSelectionListener(){
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationName = locations.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setUnitsSpinnerSelectionListener(){
        unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        units = "c";
                        break;
                    case 1:
                        units = "f";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLocation = locationText.getText().toString();
                locations.add(newLocation);
                setLocationSpinnerEntries();
                locationSpinner.setSelection(locations.indexOf(newLocation));
                saveToXML(new File(getFilesDir(), "locations.xml"), locations);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(MainActivity.LOCATION, locationName);
                data.putExtra(MainActivity.UNITS, units);
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

    private void update() {
        Intent intent = getIntent();
        locationName = intent.getStringExtra(MainActivity.LOCATION);
        units = intent.getStringExtra(MainActivity.UNITS);
        freq = intent.getIntExtra(MainActivity.FREQ, 15);
        locationSpinner.setSelection(locations.indexOf(locationName));
        unitsSpinner.setSelection(units.equals("c") ? 0 : 1);
        freqSpinner.setSelection(FREQS.indexOf(freq));
    }

    private List<String> createList(){
        List<String> list;
        File locationsFile = new File(getFilesDir(), "locations.xml");
        if (locationsFile.exists()) {
            list = readFromXML(locationsFile);
        } else {
            list = new ArrayList<>();
            list.add("Lodz, PL");
            list.add("Berlin, DE");
            list.add("Paris, FR");
            list.add("London, UK");
            list.add("New York, US");
            saveToXML(locationsFile, list);
        }
        return list;
    }

    private void saveToXML(File file, List<String> list){
        try {
            FileWriter writer = new FileWriter(file);
            XmlSerializer xml = Xml.newSerializer();
            xml.setOutput(writer);
            xml.startDocument("UTF-8", true);
            xml.startTag("", "locations");
            for(String location: list){
                xml.startTag("", "locationName");
                xml.text(location);
                xml.endTag("", "locationName");
            }
            xml.endTag("", "locations");
            xml.endDocument();
        } catch (IOException e) {
            Toast.makeText(this, R.string.file_write_error, Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> readFromXML(File file){
        List<String> list = new ArrayList<>();
        try {
            FileReader reader = new FileReader(file);
            XmlPullParser xml = Xml.newPullParser();
            xml.setInput(reader);
            int eventType = xml.getEventType();
            while(eventType != END_DOCUMENT){
                if (eventType == TEXT)
                    list.add(xml.getText());
                eventType = xml.next();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.file_read_error, Toast.LENGTH_SHORT).show();
        }
        return list;
    }
}
