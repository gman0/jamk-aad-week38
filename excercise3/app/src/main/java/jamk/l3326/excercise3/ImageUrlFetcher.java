package jamk.l3326.excercise3;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ImageUrlFetcher {
    public static ArrayList<String> fetch(URL url) {
        ArrayList<String> urls = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            final String pattern = ":thumbnail url=\"";
            String line;
            while (!(line = in.readLine()).contains("</rss>")) {
                int begin = line.lastIndexOf(pattern);
                if (begin == -1)
                    continue;

                begin += pattern.length();
                int end = line.indexOf("\"", begin);
                urls.add(line.substring(begin, end));
            }

        } catch (IOException e) {
            Log.e("<<ImageUrlFetcher>>", e.getMessage());
        }

        System.out.println(urls);

        return urls;
    }
}
