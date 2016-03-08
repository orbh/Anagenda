package myschedule.myschedule;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

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
        LoadSchedule();
    }

    public void LoadSchedule() {
        try {
            File input = new File("/tmp/input.html");
            Document doc = Jsoup.parse(input, "UTF-8", "http://schema.oru.se/setup/jsp/Schema.jsp?startDatum" +
                    "=idag&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=" +
                    "k.FE100G-26076V16-");
            Elements title = doc.getElementsByClass("data");
            for (Element link : title) {
                twTitle.setText(link.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOExeption", "IO" + e);
        }
    }
}