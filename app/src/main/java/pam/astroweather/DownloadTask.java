package pam.astroweather;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

class DownloadTask extends AsyncTask<String, Void, String> {

    private Activity activity;

    public DownloadTask(Activity activity){
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String locationName = params[0].replace(", ", "%2C%20");
        String units = params[1];
        String urlString = "https://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20weather.forecast%20where%20woeid%20in%20" +
                "(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + locationName + "%22)%20" +
                "and%20u%3D%22" + units + "%22&format=json";
        String result = download(urlString);
        //String result = "{\"query\":{\"count\":1,\"created\":\"2017-06-10T11:09:55Z\",\"lang\":\"pl\",\"results\":{\"channel\":{\"units\":{\"distance\":\"km\",\"pressure\":\"mb\",\"speed\":\"km/h\",\"temperature\":\"C\"},\"title\":\"Yahoo! Weather - Lodz, Lodz, PL\",\"link\":\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-505120/\",\"description\":\"Yahoo! Weather for Lodz, Lodz, PL\",\"language\":\"en-us\",\"lastBuildDate\":\"Sat, 10 Jun 2017 01:09 PM CEST\",\"ttl\":\"60\",\"location\":{\"city\":\"Lodz\",\"country\":\"Poland\",\"region\":\" Lodz\"},\"wind\":{\"chill\":\"66\",\"direction\":\"315\",\"speed\":\"22.53\"},\"atmosphere\":{\"humidity\":\"58\",\"pressure\":\"33592.99\",\"rising\":\"0\",\"visibility\":\"25.91\"},\"astronomy\":{\"sunrise\":\"4:24 am\",\"sunset\":\"9:0 pm\"},\"image\":{\"title\":\"Yahoo! Weather\",\"width\":\"142\",\"height\":\"18\",\"link\":\"http://weather.yahoo.com\",\"url\":\"http://l.yimg.com/a/i/brand/purplelogo//uh/us/news-wea.gif\"},\"item\":{\"title\":\"Conditions for Lodz, Lodz, PL at 12:00 PM CEST\",\"lat\":\"51.761742\",\"long\":\"19.46801\",\"link\":\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-505120/\",\"pubDate\":\"Sat, 10 Jun 2017 12:00 PM CEST\",\"condition\":{\"code\":\"26\",\"date\":\"Sat, 10 Jun 2017 12:00 PM CEST\",\"temp\":\"19\",\"text\":\"Cloudy\"},\"forecast\":[{\"code\":\"39\",\"date\":\"10 Jun 2017\",\"day\":\"Sat\",\"high\":\"20\",\"low\":\"13\",\"text\":\"Scattered Showers\"},{\"code\":\"30\",\"date\":\"11 Jun 2017\",\"day\":\"Sun\",\"high\":\"23\",\"low\":\"11\",\"text\":\"Partly Cloudy\"},{\"code\":\"28\",\"date\":\"12 Jun 2017\",\"day\":\"Mon\",\"high\":\"24\",\"low\":\"13\",\"text\":\"Mostly Cloudy\"},{\"code\":\"30\",\"date\":\"13 Jun 2017\",\"day\":\"Tue\",\"high\":\"20\",\"low\":\"12\",\"text\":\"Partly Cloudy\"},{\"code\":\"28\",\"date\":\"14 Jun 2017\",\"day\":\"Wed\",\"high\":\"20\",\"low\":\"12\",\"text\":\"Mostly Cloudy\"},{\"code\":\"30\",\"date\":\"15 Jun 2017\",\"day\":\"Thu\",\"high\":\"21\",\"low\":\"10\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"16 Jun 2017\",\"day\":\"Fri\",\"high\":\"21\",\"low\":\"13\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"17 Jun 2017\",\"day\":\"Sat\",\"high\":\"20\",\"low\":\"12\",\"text\":\"Partly Cloudy\"},{\"code\":\"23\",\"date\":\"18 Jun 2017\",\"day\":\"Sun\",\"high\":\"21\",\"low\":\"11\",\"text\":\"Breezy\"},{\"code\":\"30\",\"date\":\"19 Jun 2017\",\"day\":\"Mon\",\"high\":\"22\",\"low\":\"10\",\"text\":\"Partly Cloudy\"}],\"description\":\"<![CDATA[<img src=\\\"http://l.yimg.com/a/i/us/we/52/26.gif\\\"/>\\n<BR />\\n<b>Current Conditions:</b>\\n<BR />Cloudy\\n<BR />\\n<BR />\\n<b>Forecast:</b>\\n<BR /> Sat - Scattered Showers. High: 20Low: 13\\n<BR /> Sun - Partly Cloudy. High: 23Low: 11\\n<BR /> Mon - Mostly Cloudy. High: 24Low: 13\\n<BR /> Tue - Partly Cloudy. High: 20Low: 12\\n<BR /> Wed - Mostly Cloudy. High: 20Low: 12\\n<BR />\\n<BR />\\n<a href=\\\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-505120/\\\">Full Forecast at Yahoo! Weather</a>\\n<BR />\\n<BR />\\n(provided by <a href=\\\"http://www.weather.com\\\" >The Weather Channel</a>)\\n<BR />\\n]]>\",\"guid\":{\"isPermaLink\":\"false\"}}}}}}";
        try {
            File file = new File(activity.getFilesDir(), locationName + ".json");
            FileWriter writer = new FileWriter(file);
            writer.write(result);
            writer.close();
        } catch(IOException e){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, R.string.file_write_error, Toast.LENGTH_LONG).show();
                }
            });
        }
        return result;
    }

    private String download(String urlString){
        byte[] buffer = new byte[5000];
        int length = 0;
        try {
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
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
