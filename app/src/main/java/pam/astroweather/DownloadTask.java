package pam.astroweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadTask extends AsyncTask<String, Void, File> {

    private AppCompatActivity activity;

    public DownloadTask(AppCompatActivity activity){
        this.activity = activity;
    }

    @Override
    protected File doInBackground(String... params) {
        String locationName = params[0];
        String urlString = "https://query.yahooapis.com/v1/public/yql" +
                "?q=select * from weather.forecast where woeid in " +
                "(select woeid from geo.places(1) where text=\"" + locationName + "\")&format=json";
        String result = download(urlString);
        try {
            File file = new File(activity.getFilesDir(), locationName + ".json");
            FileWriter writer = new FileWriter(file);
            writer.write(result);
            return file;
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    private String download(String urlString){
        byte[] buffer = new byte[5000];
        int length = 0;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            InputStream input = connection.getInputStream();
            length = input.read(buffer);
        } catch(IOException e){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, R.string.download_error, Toast.LENGTH_LONG).show();
                }
            });
        }
        return new String(buffer, 0, length);
    }
}
