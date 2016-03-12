package myschedule.myschedule;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class MySchedules extends AppCompatActivity {

    //Toolbar
    Toolbar toolbar;

    //Adapter for saved schedules-list
    SavedScheduleAdapter savedScheduleAdapter;

    //Stuff to do with the list with saved schedules
    ListView savedSchedulelistView;
    List<Element> elementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedules);

        //Toolbar
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_title_myschedules));

        //Listview
        savedSchedulelistView = (ListView) findViewById(R.id.listview_saved_schedules);
        savedScheduleAdapter = new SavedScheduleAdapter(this, elementList);
        savedSchedulelistView.setAdapter(savedScheduleAdapter);

        LoadSavedSchedules();
    }

    //Creates additional items in toolbar
    @Override public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.removeItem(R.id.action_favourite);
        return true;
    }

    public void ChangeActivityToTestclass(View v) {
        Intent intent = new Intent(this, TESTKLASS.class);
        startActivity(intent);
    }

    public void LoadSavedSchedules() {
        //ToDo Connect to saved list of schedules
    }

}