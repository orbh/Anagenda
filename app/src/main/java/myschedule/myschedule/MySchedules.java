package myschedule.myschedule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MySchedules extends AppCompatActivity {

    //ScheduleHelper
    ScheduleHelper scheduleHelper;

    private static Context mContext;

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }

    //Toolbar
    Toolbar toolbar;

    //Adapter for saved schedules-list
    RecyclerView Rview;
    RecyclerView.Adapter RAdapter;
    RecyclerView.LayoutManager RLayoutManager;

    //Stuff to do with the list with saved schedules
    ListView savedScheduleListView;
    List<Schedule> scheduleList = new ArrayList<>();
    List<Elements> elementList = new ArrayList<>();

    //Runs when you first start the app
         @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedules);

        mContext = getApplicationContext();

        scheduleList = new ArrayList<>();


        //ScheduleHelper
        scheduleHelper = new ScheduleHelper();

             //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_title_myschedules));

        Rview = (RecyclerView)findViewById(R.id.saved_schedules_recycler_view);
        RLayoutManager = new LinearLayoutManager(this);
        Rview.setLayoutManager(RLayoutManager);

        RAdapter = new SavedScheduleAdapter(scheduleList,this);
        Rview.setAdapter(RAdapter);


        }

        //ListView + adapter = true
        //savedScheduleListView = (ListView) findViewById(R.id.listview_saved_schedules);
      //  savedScheduleAdapter = new SavedScheduleAdapter(this, scheduleList);
       // savedScheduleListView.setAdapter(savedScheduleAdapter);
      //  savedScheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       //     @Override
      //      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       //        LoadSelectedSchedule(view, position);

    //    });
  //  }

    //Runs every time the activity gets visible
    @Override
    protected void onResume() {
        super.onResume();
        LoadSavedSchedules();
        CheckDocumentList();
        //ToDo Sets it to refresh even if i dont have to
        RAdapter.notifyDataSetChanged();

        //OBS JUST FOR TEST PURPOSES
        //Sets one of 3 default schedules for button, until we have a search function
        List<String> defaultSchedules = new ArrayList<>();
        defaultSchedules.add(getResources().getString(R.string.default_schedule1));
        defaultSchedules.add(getResources().getString(R.string.default_schedule2));
        defaultSchedules.add(getResources().getString(R.string.default_schedule3));
        defaultSchedules.add(getResources().getString(R.string.default_schedule4));
        defaultSchedules.add(getResources().getString(R.string.default_schedule5));
        defaultSchedules.add(getResources().getString(R.string.default_schedule6));

        Random random = new Random();
        //Using the 2 first since 3 isn't working atm
        int number = random.nextInt(6);
        String url = defaultSchedules.get(number);

        Schedule schedule = ((Schedule) getApplicationContext());
        schedule.setUrl(url);
        for (Elements element:elementList)
              {
                  System.out.println(element.toString());
        }
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
                GoToPreferences();
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
        schedule.setType(scheduleList.get(position).getType());

        Intent intent = new Intent(this, TESTKLASS.class);
        startActivity(intent);
    }

    //JUST UNTIL SEARCH IS IMPLEMENTED
    //ToDo Delete afterwards
    public void ChangeActivityToTestclass(View v) {
        Schedule schedule = ((Schedule) getApplicationContext());
        Schedule tempSchedule = scheduleHelper.FetchSchedule(schedule.getUrl());
        schedule.setUrl(tempSchedule.getUrl());
        schedule.setType(tempSchedule.getType());
        schedule.setDocument(tempSchedule.getDocument());

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
                schedule.setType(scheduleHelper.getScheduleType(schedule));
                scheduleList.add(schedule);


            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOEXception", "IOException" + e);
            }

        }

    }

    //Enables empty state
    public void CheckDocumentList() {
        LinearLayout linear = (LinearLayout)findViewById(R.id.layout_content1);
        if (scheduleList.isEmpty()) {

            linear.setBackgroundResource(R.drawable.android);
           // savedScheduleListView.setBackgroundResource(R.drawable.android);
        } else {
            linear.setBackgroundResource(0);
          //  savedScheduleListView.setBackgroundResource(0);
        }
    }

    public void RefreshSchedules() {
        //ToDo Should fetch new schedules from the ones saved
    }

    public void GoToPreferences() {

        Intent intent = new Intent(this, PrefActivity.class);
        startActivity(intent);
    }






}