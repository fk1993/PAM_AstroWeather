package pam.astroweather;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private BasicInfoFragment fragment;

    public ImageDownloadTask(BasicInfoFragment fragment){
        this.fragment = fragment;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        try {
            InputStream input = new URL(url).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();
            return bitmap;
        } catch (IOException e) {
            final Activity activity = fragment.getActivity();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, R.string.download_error, Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        fragment.image.setImageBitmap(bitmap);
    }
}
