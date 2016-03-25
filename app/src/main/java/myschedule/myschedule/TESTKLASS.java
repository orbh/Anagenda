package myschedule.myschedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TESTKLASS extends AppCompatActivity {

    Toolbar toolbar;
    //ListView lwSchedule;

    RecyclerView mRecycleView;
    RecyclerView.Adapter mRecycleAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //CustomScheduleAdapter customScheduleAdapter;

    //Contains all "rows" of posts
    List<Element> elementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testklass_layout);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecycleView = (RecyclerView) findViewById(R.id.schedule_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        mRecycleAdapter = new ScheduleAdapter(elementList);
        mRecycleView.setAdapter(mRecycleAdapter);

        /*
        //Schedule with custom adapter
        lwSchedule = (ListView) findViewById(R.id.lwSchedule);
        customScheduleAdapter = new CustomScheduleAdapter(this, elementList);
        lwSchedule.setAdapter(customScheduleAdapter);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshList();
        LoadSchedule();
    }

    //Creates additional items in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.removeItem(R.id.action_refresh);
        menu.removeItem(R.id.action_search);
        return true;
    }

    //Triggers code when toolbar-items are clicked
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

    public void LoadSchedule() {

        Schedule schedule = ((Schedule)getApplicationContext());
        Document document = schedule.getDocument();

        //ToDo This might crash shit up
        if (schedule.getType() == 0) {
            return;
        }

        //Fetches table with only schedule rows
        Elements posts = document.select("table.schemaTabell > tbody > tr.data-white, tr.data-grey");

        //Puts each row into a list
        for (Element element : posts) {
            elementList.add(element);
        }

        //Sets the toolbar title
        String title = schedule.getDocument().select("td.big2 > table > tbody > tr > td").get(1).text();
        SetTitle(title, schedule.getType());
    }

    //Attempts to save schedule
    public void SaveSchedule() {

        //Writes document to file
        try {

            Schedule schedule = ((Schedule)getApplicationContext());
            String input = schedule.getDocument().select("td.big2 > table > tbody > tr > td").get(1).text();
            String output = input.substring(0, input.indexOf(","));
            schedule.getDocument().setBaseUri(schedule.getUrl());

            FileOutputStream fos = openFileOutput(output, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(schedule.getDocument().toString());
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

    //Sets the toolbar title
    public void SetTitle(String name, int type){

        //Course
        if(type == 1) {

            String output = name.substring(name.indexOf(",") + 1);
            assert getSupportActionBar() != null;
            if (output.equals("")) {
                getSupportActionBar().setTitle(getResources().getString(R.string.schedule_title));
            } else getSupportActionBar().setTitle(output);

        }

        //Room, Signature and Programme
        else {
            assert getSupportActionBar() != null;
            if (name.equals("")) {
                getSupportActionBar().setTitle(getResources().getString(R.string.schedule_title));
            } else getSupportActionBar().setTitle(name);
        }

    }

    public void RefreshList(){
        elementList.clear();
        //customScheduleAdapter.notifyDataSetChanged();
    }

}