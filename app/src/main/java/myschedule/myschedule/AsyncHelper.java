package myschedule.myschedule;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

//Should probably not be public abstract, but cant get it working otherwise
class AsyncHelper extends AsyncTask<String, Void, Document> {

    protected Document doInBackground(String... urls) {
        try {
            return Jsoup.connect(urls[0]).get();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOExeption", "IOExeption" + e);
            return null;
        }
    }

    protected void onPostExecute(Document document) {
    }
}
