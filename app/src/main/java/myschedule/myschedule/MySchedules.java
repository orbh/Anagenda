package myschedule.myschedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import android.view.MotionEvent;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener.SwipeListener;

public class MySchedules extends AppCompatActivity {

    //Context
    Context mContext;

    //ScheduleHelper
    ScheduleHelper scheduleHelper;

    //Toolbar
    Toolbar toolbar;


    //Adapter for saved schedules-list
    RecyclerView recyclerView;
    RecyclerView.Adapter RAdapter;
    RecyclerView.LayoutManager RLayoutManager;

    //Stuff to do with the list with saved schedules
    ListView savedScheduleListView;
    List<Schedule> scheduleList = new ArrayList<>();
    List<Elements> elementList = new ArrayList<>();

    private SavedScheduleAdapter adapter;

    //Runs when you first start the app
         @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedules);

        //ScheduleHelper
        scheduleHelper = new ScheduleHelper();

             //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_title_myschedules));

        recyclerView = (RecyclerView)findViewById(R.id.saved_schedules_recycler_view);
        RLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(RLayoutManager);

        RAdapter = new SavedScheduleAdapter(scheduleList,this);
        recyclerView.setAdapter(RAdapter);

             initSwipe();
         //    adapter.notifyDataSetChanged();


    };




        //ListView + adapter = true
     //   savedScheduleListView = (ListView) findViewById(R.id.listview_saved_schedules);
     //   savedScheduleAdapter = new SavedScheduleAdapter(this, scheduleList);
     //   savedScheduleListView.setAdapter(savedScheduleAdapter);
     //   savedScheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     //       @Override
    //        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //            LoadSelectedSchedule(view, position);
    //        }
    //    });


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

                ScheduleFile scheduleFile;
                FileInputStream fin = new FileInputStream(testFile);
                ObjectInputStream ois = new ObjectInputStream(fin);
                scheduleFile = (ScheduleFile) ois.readObject();
                ois.close();

                Schedule schedule = new Schedule();
                schedule.setType(scheduleFile.getType());
                schedule.setUrl(scheduleFile.getUrl());
                schedule.setDocument(Jsoup.parse(testFile, "UTF-8", scheduleFile.getUrl()));

                System.out.println(schedule.getUrl());
                System.out.println(schedule.getType());
                //Document document = Jsoup.parse(testFile, "UTF-8", emptyUri);

                /*
                schedule.setUrl(document.baseUri());
                schedule.setDocument(document);
                schedule.setType(scheduleHelper.getScheduleType(schedule));
                */
                scheduleList.add(schedule);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOEXception", "IOException" + e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.e("ClassNotFoundException", "ClassNotFoundException" + e);
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

    public void UpdateSchedule() {

    }

    public void UpdateAllSchedules() {
        UpdateSchedule();
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    adapter.removeItem(position);
                }
                else if
                    (direction == ItemTouchHelper.RIGHT) {
                    return;
                }
            }

};
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}