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
        String locationName = params[0];
        String units = params[1];
        String urlString = "https://query.yahooapis.com/v1/public/yql" +
                "?q=select * from weather.forecast where woeid in " +
                "(select woeid from geo.places(1) where text=\"" + locationName + "\") " +
                "and u=\"" + units + "\"&format=json";
        String result = download(urlString);
        //String result = "{\"query\":{\"count\":1,\"created\":\"2017-06-04T11:08:03Z\",\"lang\":\"pl\",\"results\":{\"channel\":{\"units\":{\"distance\":\"km\",\"pressure\":\"mb\",\"speed\":\"km/h\",\"temperature\":\"C\"},\"title\":\"Yahoo! Weather - Lodz, Lodz, PL\",\"link\":\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-505120/\",\"description\":\"Yahoo! Weather for Lodz, Lodz, PL\",\"language\":\"en-us\",\"lastBuildDate\":\"Sun, 04 Jun 2017 01:08 PM CEST\",\"ttl\":\"60\",\"location\":{\"city\":\"Lodz\",\"country\":\"Poland\",\"region\":\" Lodz\"},\"wind\":{\"chill\":\"81\",\"direction\":\"155\",\"speed\":\"28.97\"},\"atmosphere\":{\"humidity\":\"34\",\"pressure\":\"33322.08\",\"rising\":\"0\",\"visibility\":\"25.91\"},\"astronomy\":{\"sunrise\":\"4:27 am\",\"sunset\":\"8:55 pm\"},\"image\":{\"title\":\"Yahoo! Weather\",\"width\":\"142\",\"height\":\"18\",\"link\":\"http://weather.yahoo.com\",\"url\":\"http://l.yimg.com/a/i/brand/purplelogo//uh/us/news-wea.gif\"},\"item\":{\"title\":\"Conditions for Lodz, Lodz, PL at 12:00 PM CEST\",\"lat\":\"51.761742\",\"long\":\"19.46801\",\"link\":\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-505120/\",\"pubDate\":\"Sun, 04 Jun 2017 12:00 PM CEST\",\"condition\":{\"code\":\"26\",\"date\":\"Sun, 04 Jun 2017 12:00 PM CEST\",\"temp\":\"26\",\"text\":\"Cloudy\"},\"forecast\":[{\"code\":\"47\",\"date\":\"04 Jun 2017\",\"day\":\"Sun\",\"high\":\"27\",\"low\":\"11\",\"text\":\"Scattered Thunderstorms\"},{\"code\":\"28\",\"date\":\"05 Jun 2017\",\"day\":\"Mon\",\"high\":\"22\",\"low\":\"11\",\"text\":\"Mostly Cloudy\"},{\"code\":\"4\",\"date\":\"06 Jun 2017\",\"day\":\"Tue\",\"high\":\"28\",\"low\":\"12\",\"text\":\"Thunderstorms\"},{\"code\":\"47\",\"date\":\"07 Jun 2017\",\"day\":\"Wed\",\"high\":\"18\",\"low\":\"12\",\"text\":\"Scattered Thunderstorms\"},{\"code\":\"30\",\"date\":\"08 Jun 2017\",\"day\":\"Thu\",\"high\":\"18\",\"low\":\"9\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"09 Jun 2017\",\"day\":\"Fri\",\"high\":\"24\",\"low\":\"11\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"10 Jun 2017\",\"day\":\"Sat\",\"high\":\"21\",\"low\":\"12\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"11 Jun 2017\",\"day\":\"Sun\",\"high\":\"23\",\"low\":\"12\",\"text\":\"Partly Cloudy\"},{\"code\":\"47\",\"date\":\"12 Jun 2017\",\"day\":\"Mon\",\"high\":\"25\",\"low\":\"15\",\"text\":\"Scattered Thunderstorms\"},{\"code\":\"30\",\"date\":\"13 Jun 2017\",\"day\":\"Tue\",\"high\":\"22\",\"low\":\"15\",\"text\":\"Partly Cloudy\"}],\"description\":\"<![CDATA[<img src=\\\"http://l.yimg.com/a/i/us/we/52/26.gif\\\"/>\\n<BR />\\n<b>Current Conditions:</b>\\n<BR />Cloudy\\n<BR />\\n<BR />\\n<b>Forecast:</b>\\n<BR /> Sun - Scattered Thunderstorms. High: 27Low: 11\\n<BR /> Mon - Mostly Cloudy. High: 22Low: 11\\n<BR /> Tue - Thunderstorms. High: 28Low: 12\\n<BR /> Wed - Scattered Thunderstorms. High: 18Low: 12\\n<BR /> Thu - Partly Cloudy. High: 18Low: 9\\n<BR />\\n<BR />\\n<a href=\\\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-505120/\\\">Full Forecast at Yahoo! Weather</a>\\n<BR />\\n<BR />\\n(provided by <a href=\\\"http://www.weather.com\\\" >The Weather Channel</a>)\\n<BR />\\n]]>\",\"guid\":{\"isPermaLink\":\"false\"}}}}}}";
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
