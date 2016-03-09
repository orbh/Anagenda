package myschedule.myschedule;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TESTKLASS extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ListView lwSchedule;

    CustomScheduleAdapter customScheduleAdapter;
    //List<String> postList = new ArrayList<>();
    List<Element> elementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testklass_layout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        lwSchedule = (ListView)findViewById(R.id.lwSchedule);
        //scheduleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, postList);
        //lwSchedule.setAdapter(scheduleAdapter);
        customScheduleAdapter = new CustomScheduleAdapter(this, elementList);
        lwSchedule.setAdapter(customScheduleAdapter);

        LoadSchedule();
    }

    public void LoadSchedule() {
        try {

            Document document = new AsyncHelper().execute(getResources().getString(R.string.default_schedule)).get();

            //Fetches table with only schedule rows
            Elements posts = document.select("table.schemaTabell > tbody > tr.data-white, tr.data-grey");

            //Fetches the description of the post and adds it to the postList
            for (Element element : posts) {

                elementList.add(element);
                //postList.add(element.child(9).text());
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

}