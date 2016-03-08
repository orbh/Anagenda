package myschedule.myschedule;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;

public class TESTKLASS extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView twTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testklass_layout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        twTitle = (TextView) findViewById(R.id.twScheduleTitle);

        try {
            Document document = new AsyncHelper().execute(getResources().getString(R.string.default_schedule)).get();
            Elements titles = document.getElementsByClass("data");
            for (Element title : titles) {
                twTitle.setText(title.text());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("InterruptedException", "InterruptedException" + e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("ExecutionException", "ExecutionExeption" + e);
        }
    }

}