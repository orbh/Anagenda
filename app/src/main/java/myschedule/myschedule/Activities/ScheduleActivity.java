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
import myschedule.myschedule.Objects.SchedulePost;
import myschedule.myschedule.R;
import myschedule.myschedule.Utilities.ScheduleHelper;

public class ScheduleActivity extends AppCompatActivity {

    boolean saved;

    Toolbar toolbar;
    Context mcontext;

    ScheduleHelper scheduleHelper;

    RecyclerView mRecycleView;
    RecyclerView.Adapter mRecycleAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    Schedule activeSchedule;
    ArrayList<SchedulePost> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        mcontext = this;

        scheduleHelper = new ScheduleHelper();

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecycleView = (RecyclerView) findViewById(R.id.schedule_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        mRecycleAdapter = new ScheduleAdapter(postList, this);
        mRecycleView.setAdapter(mRecycleAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshList();
        LoadSchedule();
        mRecycleAdapter.notifyDataSetChanged();
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
        saved = CheckExisting(schedule.getCode(), schedule.getName());

        if (saved)
        {
            menu.findItem(R.id.action_favourite).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
            return true;
        }
        else
        {
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

    //Loads schedule from global object and sets up the dataset for recyclerview
    public void LoadSchedule() {

        activeSchedule = ((Schedule) getApplicationContext());
        for (SchedulePost schedulePost: activeSchedule.getPostList()) {
            postList.add(schedulePost);
        }

        //postList = activeSchedule.getPostList();

        //Sets toolbar title
        assert getSupportActionBar() != null;
        if (activeSchedule.getName().equals("")) {
            getSupportActionBar().setTitle(getResources().getString(R.string.schedule_title));
        }
        else
        {
            getSupportActionBar().setTitle(activeSchedule.getName());
        }
        mRecycleAdapter.notifyDataSetChanged();
    }

    //Attempts to save schedule
    public void SaveSchedule() {

        //Writes document to file
        Schedule activeSchedule = ((Schedule) getApplicationContext());
        scheduleHelper.SaveSchedule(activeSchedule, getApplicationContext());

        //Makes a toast if it succeeds
        Toast.makeText(ScheduleActivity.this, getResources().getString(R.string.toast_saved_schedule), Toast.LENGTH_SHORT).show();
    }

    public void RefreshList() {
        try {
            postList.clear();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" +e);
        }
    }

    // Deletes the currently active schedule
    public void callDeleteSchedule() {

        Schedule scheduleToDelete = ((Schedule) mcontext.getApplicationContext());
        scheduleHelper.DeleteSchedule(scheduleToDelete, mcontext);

        saved = false;
        Toast.makeText(ScheduleActivity.this,
                getResources().getString(R.string.toast_delete_schedule),
                Toast.LENGTH_SHORT).show();

        mRecycleAdapter.notifyDataSetChanged();

        //Sends user back to homeactivity and clears the ScheduleActivity
        //from the activity list
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    //Checks if schedule is already saved
    public boolean CheckExisting(String code, String name) {

        File files[] = getFilesDir().listFiles();

        for (File f : files) {
            if (f.getName().equals(code) || f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}