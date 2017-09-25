package jamk.l3326.excercise3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ShowImages extends AppCompatActivity {

    private class FetchTask extends AsyncTask<URL, Void, Void> {
        ArrayList<String> imageUrls;

        @Override
        protected Void doInBackground(URL... urls) {
            imageUrls = ImageUrlFetcher.fetch(urls[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            GridView gv = (GridView)findViewById(R.id.gridView);
            gv.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return imageUrls.size();
                }

                @Override
                public Object getItem(int i) {
                    return imageUrls.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int pos, View convertView, ViewGroup parent) {
                    SquaredImageView view = (SquaredImageView)convertView;
                    if (view == null) {
                        view = new SquaredImageView(getApplicationContext());
                        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                    String url = (String)getItem(pos);

                    Picasso.with(getApplicationContext())
                            .load(url)
                            .placeholder(android.R.color.darker_gray)
                            .error(android.R.color.holo_red_dark)
                            .fit()
                            .tag(getApplicationContext())
                            .into(view);

                    return view;
                }
            });

            Toast.makeText(getApplicationContext(), "" + imageUrls.size(), Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

        String tags = getIntent().getExtras().getString("tags");
        URL url = null;
        try {
            url = new URL("https://backend.deviantart.com/rss.xml?type=deviation&q=" + tags);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new FetchTask().execute(url);
    }
}
