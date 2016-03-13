package myschedule.myschedule;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TESTKLASS extends AppCompatActivity {

    Toolbar toolbar;
    ListView lwSchedule;
    Document loadedDocument;

    CustomScheduleAdapter customScheduleAdapter;

    //Contains all "rows" of posts
    List<Element> elementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testklass_layout);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Schedule with custom adapter
        lwSchedule = (ListView) findViewById(R.id.lwSchedule);
        customScheduleAdapter = new CustomScheduleAdapter(this, elementList);
        lwSchedule.setAdapter(customScheduleAdapter);

        LoadSchedule();
        Toast.makeText(TESTKLASS.this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    //Creates additional items in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.removeItem(R.id.action_refresh);
        menu.removeItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //ToDo Make it go to settings menu
            case R.id.action_settings:
                return true;

            case R.id.action_favourite:
                SaveSchedule();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public Document FetchSchedule(String url) {
        try {
            return new AsyncKronoXHelper().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("InterruptedException", "InterruptedException" + e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("ExecutionException", "ExecutionExeption" + e);
        }
        //ToDo Maybe null has to be something else
        return null;
    }

    public void LoadSchedule() {
        //ToDo Should take URL from loaded schedule
        String url = getResources().getString(R.string.default_schedule1);
        loadedDocument = FetchSchedule(url);

        //ToDo A course is differs from a teacher-view, which differs from a locale-view which
        //ToDo means that we have to customize the numbers that gets the selectors at least
        //ToDo and maybe even the selects themselves

        //Fetches table with only schedule rows
        Elements posts = loadedDocument.select("table.schemaTabell > tbody > tr.data-white, tr.data-grey");

        //Puts each row into a list
        for (Element element : posts) {
            elementList.add(element);
        }

        //Sets the name of toolbar
        Element title = loadedDocument.select("td.big2 > table > tbody > tr > td").get(1);
        String input = title.text();
        String output = input.substring(input.indexOf(",") + 1);
        assert getSupportActionBar() != null;
        if (output.equals("")) {
            getSupportActionBar().setTitle("Schema");
        } else getSupportActionBar().setTitle(output);
    }

    public void SaveSchedule() {

        //Writes document to file
        try {

            String input = loadedDocument.select("td.big2 > table > tbody > tr > td").get(1).text();
            String output = input.substring(0, input.indexOf(","));

            FileOutputStream fos = openFileOutput(output, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(loadedDocument.outerHtml());
            oos.close();
            fos.close();

            //Makes a toast if it succeeds
            Toast.makeText(TESTKLASS.this, getResources().getString(R.string.toast_saved_schedule) + " " + output, Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", "FileNotFoundException" + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "IOException" + e);
        }

    }

}