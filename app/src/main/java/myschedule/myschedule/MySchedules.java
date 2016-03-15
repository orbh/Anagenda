package myschedule.myschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MySchedules extends AppCompatActivity {

    //Toolbar
    Toolbar toolbar;

    //Adapter for saved schedules-list
    SavedScheduleAdapter savedScheduleAdapter;

    //Stuff to do with the list with saved schedules
    ListView savedScheduleListView;
    List<Schedule> scheduleList = new ArrayList<>();

    //Runs when you first start the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedules);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_title_myschedules));

        //ListView + adapter = true
        savedScheduleListView = (ListView) findViewById(R.id.listview_saved_schedules);
        savedScheduleAdapter = new SavedScheduleAdapter(this, scheduleList);
        savedScheduleListView.setAdapter(savedScheduleAdapter);
        savedScheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LoadSelectedSchedule(view, position);
            }
        });
    }

    //Runs every time the activity gets visible
    @Override
    protected void onResume() {
        super.onResume();
        LoadSavedSchedules();
        CheckDocumentList();
        //ToDo Sets it to refresh even if i dont have to
        savedScheduleAdapter.notifyDataSetChanged();

        //OBS JUST FOR TEST PURPOSES
        //Sets one of 3 default schedules for button, until we have a search function
        List<String> defaultSchedules = new ArrayList<>();
        defaultSchedules.add(getResources().getString(R.string.default_schedule1));
        defaultSchedules.add(getResources().getString(R.string.default_schedule2));
        defaultSchedules.add(getResources().getString(R.string.default_schedule3));
        Random random = new Random();
        //Using the 2 first since 3 isn't working atm
        int number = random.nextInt(3);
        String url = defaultSchedules.get(number);

        Schedule schedule = ((Schedule) getApplicationContext());
        schedule.setUrl(url);
    }

    //Creates additional items in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.removeItem(R.id.action_favourite);
        return true;
    }

    //Makes the items in toolbar call methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //ToDo Make it go to settings menu
            case R.id.action_settings:
                return true;

            case R.id.action_refresh:
                RefreshSchedules();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void LoadSelectedSchedule(View v, int position) {

        Schedule schedule = ((Schedule) getApplicationContext());
        schedule.setDocument(scheduleList.get(position).getDocument());
        schedule.setUrl(scheduleList.get(position).getUrl());

        Intent intent = new Intent(this, TESTKLASS.class);
        startActivity(intent);
    }

    public void ChangeActivityToTestclass(View v) {
        Schedule schedule = ((Schedule) getApplicationContext());
        schedule.setDocument(FetchSchedule(schedule.getUrl()));
        Intent intent = new Intent(this, TESTKLASS.class);
        startActivity(intent);
    }

    //Loads saved schedules from files
    public void LoadSavedSchedules() {
        //ToDo Connect to saved list of schedules

        //Clears the documentList
        scheduleList.clear();

        //Fetches all files in the files directory

        //ToDo Use fileList instead!
        File childFile[] = getFilesDir().listFiles();

        //Tries to make the files into documents again and push them into the documentList
        for (File file : childFile) {

            File testFile = new File(file.getPath());

            //ToDo Maybe we can be able to fetch the URL, if necessary
            String emptyUri = "";

            try {
                Document document = Jsoup.parse(testFile, "UTF-8", emptyUri);
                Schedule schedule = new Schedule();
                schedule.setUrl(document.baseUri());
                schedule.setDocument(document);

                scheduleList.add(schedule);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOEXception", "IOException" + e);
            }

        }

    }

    public void CheckDocumentList() {

        if (scheduleList.isEmpty()) {
            savedScheduleListView.setBackgroundResource(R.drawable.ic_snooze_red_500_18dp);
        } else {
            savedScheduleListView.setBackgroundResource(0);
        }
    }

    public void RefreshSchedules() {
        //ToDo Should fetch new schedules from the ones saved
    }

    //JUST FOR TEST PURPOSES
    //Here until we get search function going
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

}