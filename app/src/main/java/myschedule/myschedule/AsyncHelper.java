package myschedule.myschedule;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

//Asynctask that fetches the schedule
class AsyncHelper extends AsyncTask<String, Void, Document> {

    //Connects to specified url and returns a document
    protected Document doInBackground(String... urls) {
        try {
            return Jsoup.connect(urls[0]).get();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOExeption", "IOExeption" + e);
            return null;
        }
    }

    //For tasks that have to happen after doInBackground
    protected void onPostExecute(Document document) {
    }
}
