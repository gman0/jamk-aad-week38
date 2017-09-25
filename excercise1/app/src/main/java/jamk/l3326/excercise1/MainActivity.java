package jamk.l3326.excercise1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {



    private ImageView imageView;
    // text view object
    private TextView textView;
    // progress bar object
    private ProgressBar progressBar;
    // example image names (change to your own if needed...)
    private String[] images = {"https://imgs.xkcd.com/comics/ohm.png", "https://imgs.xkcd.com/comics/pep_talk.png"};
    // which image is now visible
    private int imageIndex;
    // swipe down and up values
    private float x1,x2;

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL imgUrl = null;
            Bitmap bm = null;

            try {
                imgUrl = new URL(urls[0]);
                InputStream is = imgUrl.openStream();
                bm = BitmapFactory.decodeStream(is);
                System.out.println(bm);
            } catch (Exception e) {
                Log.e("<<LOADIMAGE>>", e.getMessage());
            }

            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            textView.setText("Image " + (imageIndex + 1) + "/" + images.length);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    // async task to load a new image
    private DownloadImageTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        imageIndex = 0;
        textView.setText("Image " + (imageIndex + 1) + "/" + images.length);

        showImage();
    }

    private void showImage() {
        task = new DownloadImageTask();
        task.execute(images[imageIndex]);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                if (x1 < x2) {
                    imageIndex--;
                    if (imageIndex < 0) imageIndex = images.length - 1;
                } else {
                    imageIndex++;
                    if (imageIndex > (images.length - 1)) imageIndex = 0;
                }
                showImage();
                break;
        }

        return false;
    }
}
