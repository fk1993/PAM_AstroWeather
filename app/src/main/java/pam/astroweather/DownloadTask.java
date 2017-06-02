package pam.astroweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadTask extends AsyncTask<String, Void, String> {

    private AppCompatActivity activity;

    public DownloadTask(AppCompatActivity activity){
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];
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
