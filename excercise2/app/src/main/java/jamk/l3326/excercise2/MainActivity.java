package jamk.l3326.excercise2;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String mediaPath;
    private ArrayList<String> files = new ArrayList<>();
    private MediaPlayer player = new MediaPlayer();

    private class LoadSongsTask extends AsyncTask<Void, String, Void> {
        private ArrayList<String> loadedFiles = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            updateSongListRecursively(new File(mediaPath));
            return null;
        }

        public void updateSongListRecursively(File path) {
            if (path.isDirectory()) {
                for (int i = 0; i < path.listFiles().length; i++) {
                    File file = path.listFiles()[i];
                    updateSongListRecursively(file);
                }
            } else {
                String name = path.getAbsolutePath();
                publishProgress(name);

                if (name.endsWith(".mp3"))
                    loadedFiles.add(name);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter<String> songList = new ArrayAdapter<String>(
                    MainActivity.this, android.R.layout.simple_list_item_1, loadedFiles);
            listView.setAdapter(songList);
            files = loadedFiles;

            Toast.makeText(getApplicationContext(),
                    "Loaded " + files.size() + " songs", Toast.LENGTH_LONG).show();
        }
    }

    LoadSongsTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        mediaPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                try {
                    player.reset();
                    player.setDataSource(files.get(pos));
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    Log.e("<<MEDIA PLAYER>>", e.getMessage());
                    Toast.makeText(getBaseContext(), "Cannot start audio playback!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        String s = "";
        File f = new File(mediaPath);
        for (int i = 0; i < f.list().length; i++) {
            s += f.list()[i];
            s += "\n";
        }

        Toast.makeText(getApplicationContext(),
                "" + s, Toast.LENGTH_LONG).show();

        task = new LoadSongsTask();
        task.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (player.isPlaying())
            player.reset();
    }
}
