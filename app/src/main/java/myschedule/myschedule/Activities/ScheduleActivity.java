package myschedule.myschedule.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import myschedule.myschedule.Adapters.ScheduleAdapter;
import myschedule.myschedule.Objects.Schedule;
import myschedule.myschedule.Objects.ScheduleFile;
import myschedule.myschedule.R;

public class ScheduleActivity extends AppCompatActivity {
    boolean saved;

    Toolbar toolbar;
    Context mcontext;

    RecyclerView mRecycleView;
    RecyclerView.Adapter mRecycleAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //Contains all "rows" of posts
    List<Element> elementList = new ArrayList<>();

    public ScheduleActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testklass_layout);

        mcontext = this;

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecycleView = (RecyclerView) findViewById(R.id.schedule_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        mRecycleAdapter = new ScheduleAdapter(elementList, this);
        mRecycleView.setAdapter(mRecycleAdapter);
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
        menu.removeItem(R.id.action_settings);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Schedule schedule = ((Schedule) mcontext.getApplicationContext());
        String input = schedule.getDocument().select("td.big2 > table > tbody > tr > td").get(1).text();
        String output = input.substring(0, input.indexOf(","));

        File files[] = getFilesDir().listFiles();

        for (File f : files) {
            if (f.getName().equals(output)) {
                saved = true;
            }
        }

        if (saved) {

            menu.findItem(R.id.action_favourite).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);

            return true;
        } else {
            menu.findItem(R.id.action_favourite).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(false);

            return true;
        }
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
                invalidateOptionsMenu();
                return true;

            case R.id.action_delete:
                callDeleteSchedule();
                invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void LoadSchedule() {

        Schedule schedule = ((Schedule) getApplicationContext());
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

            Schedule schedule = ((Schedule) getApplicationContext());
            String input = schedule.getDocument().select("td.big2 > table > tbody > tr > td").get(1).text();
            String output = input.substring(0, input.indexOf(","));
            schedule.getDocument().setBaseUri(schedule.getUrl());
            FileOutputStream fos = openFileOutput(output, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            ScheduleFile scheduleFile = new ScheduleFile();
            scheduleFile.setUrl(schedule.getUrl());
            scheduleFile.setType(schedule.getType());
            scheduleFile.setSchedule(schedule.getDocument().toString());

            oos.writeObject(scheduleFile);
            oos.close();
            fos.close();

            //Makes a toast if it succeeds
            Toast.makeText(ScheduleActivity.this, getResources().getString(R.string.toast_saved_schedule), Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", "FileNotFoundException" + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "IOException" + e);
        }
    }

    //Sets the toolbar title
    public void SetTitle(String name, int type) {

        //Course
        if (type == 1) {

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

    public void RefreshList() {
        elementList.clear();
    }

    // Deletes the currently active schedule
    public void callDeleteSchedule() {

        try {
            File files[] = getFilesDir().listFiles();

            Schedule schedule = ((Schedule) mcontext.getApplicationContext());
            String input = schedule.getDocument().select("td.big2 > table > tbody > tr > td").get(1).text();
            String output = input.substring(0, input.indexOf(","));

            for (File f : files)
                if (f.getName().equals(output)) {

                    f.delete();

                }
            Toast.makeText(ScheduleActivity.this, getResources().getString(R.string.toast_delete_schedule), Toast.LENGTH_SHORT).show();

            saved = false;

            mRecycleAdapter.notifyDataSetChanged();

            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("IndexOutOfBoundsEx", "IndexOutOfBoundsEx" + e);
        }
    }

}