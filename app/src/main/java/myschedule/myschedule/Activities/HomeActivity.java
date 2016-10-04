package myschedule.myschedule.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import myschedule.myschedule.Objects.Schedule;
import myschedule.myschedule.R;
import myschedule.myschedule.Adapters.SavedScheduleAdapter;
import myschedule.myschedule.Utilities.ScheduleHelper;
import myschedule.myschedule.Utilities.UpdateService;


public class HomeActivity extends AppCompatActivity {

    //Context
    Context mContext;

    //Preferences
    SharedPreferences sharedPreferences;

    //ScheduleHelper
    ScheduleHelper scheduleHelper;

    //Toolbar
    Toolbar toolbar;

    //Adapter for saved schedules-list
    RecyclerView recyclerView;
    RecyclerView.Adapter RAdapter;
    RecyclerView.LayoutManager RLayoutManager;

    //Stuff to do with the list with saved schedules
    ArrayList<Schedule> scheduleList = new ArrayList<>();

    //Emptystate
    TextView textView;

    //Runs when you first start the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //Context
        mContext = this;

        //Sets default values for preferences
        //ToDo Seems like the 1st line doesn't have to be here
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //ScheduleHelper
        scheduleHelper = new ScheduleHelper();

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_title_myschedules));

        recyclerView = (RecyclerView) findViewById(R.id.saved_schedules_recycler_view);
        RLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(RLayoutManager);

        RAdapter = new SavedScheduleAdapter(scheduleList, this);
        recyclerView.setAdapter(RAdapter);
    }

    //Runs every time the activity gets visible
    @Override
    protected void onResume() {
        super.onResume();
        LoadSavedSchedules();
        CheckDocumentList();
        //ToDo Sets it to refresh even if it dont have to
        RAdapter.notifyDataSetChanged();

        //Automatic update
        ScheduleAlarm();
    }

    //Creates additional items in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.action_favourite).setVisible(false);
        menu.findItem(R.id.action_delete).setVisible(false);

        return true;
    }

    //Makes the items in toolbar call methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                GoToPreferences();
                return true;

            case R.id.action_refresh:
                UpdateAllSchedules();
                return true;

            case R.id.action_search:
                goToSearch();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToSearch() {

        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    //Loads saved schedules from files
    public void LoadSavedSchedules() {

        //Clears the documentList
        scheduleList.clear();
        for (Schedule schedule : scheduleHelper.LoadAllSchedules(getApplicationContext())) {
            scheduleList.add(schedule);
        }
    }

    //Enables empty state function
    //ToDo Change the empty state picture
    public void CheckDocumentList() {
        LinearLayout linear = (LinearLayout) findViewById(R.id.layout_content1);
        if (scheduleList.isEmpty()) {
            linear.setBackgroundResource(R.drawable.emptystate3);
        } else {
            linear.setBackgroundResource(0);
            textView = (TextView) findViewById(R.id.emptystate);
            textView.setVisibility(View.GONE);
        }
    }

    public void GoToPreferences() {
        Intent intent = new Intent(this, PrefActivity.class);
        startActivity(intent);
    }

    public void UpdateAllSchedules() {
        scheduleHelper.UpdateAllSchedules(getApplicationContext());
        LoadSavedSchedules();
        CheckDocumentList();
        RAdapter.notifyDataSetChanged();

        Toast.makeText(HomeActivity.this, getResources()
                .getString(R.string.toast_updated_schedules), Toast.LENGTH_SHORT).show();
    }

    public void ScheduleAlarm() {
        String updateInterval = sharedPreferences.getString("update_list", "24");

        if (updateInterval.equals("0")) {
            return;
        }

        Intent i = new Intent(this, UpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pi);

        long alarmInterval = 0L;

        switch (updateInterval) {
            case "24":
                alarmInterval = AlarmManager.INTERVAL_DAY;
                break;
            case "12":
                alarmInterval = AlarmManager.INTERVAL_HALF_DAY;
                break;
            case "48":
                alarmInterval = AlarmManager.INTERVAL_DAY * 2;
                break;
        }

        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + alarmInterval,
                alarmInterval, pi);
    }
}