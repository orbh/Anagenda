package myschedule.myschedule;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

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

    CustomScheduleAdapter customScheduleAdapter;

    //Contains all "rows" of posts
    List<Element> elementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testklass_layout);

        //Toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Schedule with custom adapter
        lwSchedule = (ListView)findViewById(R.id.lwSchedule);
        customScheduleAdapter = new CustomScheduleAdapter(this, elementList);
        lwSchedule.setAdapter(customScheduleAdapter);

        LoadSchedule();
    }

    public void LoadSchedule() {
        try {
            //ToDo Should take URL from loaded schedule
            //Document document = new AsyncKronoXHelper().execute(getResources().getString(R.string.default_schedule)).get();
            Document document = new AsyncKronoXHelper().execute(getResources().getString(R.string.default_schedule2)).get();

            //TESTMETHOD
            SaveSchedule(document);

            //Fetches table with only schedule rows
            Elements posts = document.select("table.schemaTabell > tbody > tr.data-white, tr.data-grey");

            //Puts each row into a list
            for (Element element : posts) {
                elementList.add(element);
            }

            //Sets the name of toolbar
            Element title = document.select("td.big2 > table > tbody > tr > td").get(1);
            String input = title.text();
            String output = input.substring(input.indexOf(",") + 1);
            assert getSupportActionBar() != null;
            if(output.equals("")){
                getSupportActionBar().setTitle("Schema");
            }
            else getSupportActionBar().setTitle(output);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("InterruptedException", "InterruptedException" + e);
        }
        catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("ExecutionException", "ExecutionExeption" + e);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerExeption", "NullPointerExeption" + e);
        }
    }


    public void SaveSchedule(Document document){

        //Writes to file-folder in app
        try {
            String input = document.select("td.big2 > table > tbody > tr > td").get(1).text();
            String output = input.substring(0, input.indexOf(","));

            //ToDo Might have to put file ending
            FileOutputStream fos = openFileOutput(output, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(document.outerHtml());
            oos.close();
            fos.close();

            /*
            BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
            htmlWriter.write(document.outerHtml());
            htmlWriter.flush();
            htmlWriter.close();
            */

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", "FileNotFoundException"+e);
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "IOException"+e);
        }

    }

}