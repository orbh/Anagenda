package myschedule.myschedule.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import myschedule.myschedule.Objects.Schedule;
import myschedule.myschedule.R;
import myschedule.myschedule.Utilities.ScheduleHelper;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Spinner spinner;
    TextView textView;
    TextView textViewInfo;
    EditText editText;

    ScheduleHelper scheduleHelper;

    Integer searchType;

    Schedule globalSchedule;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewInfo = (TextView) findViewById(R.id.searchinfo);

        spinner = (Spinner) findViewById(R.id.spinner);



        //Global schedule
        globalSchedule = ((Schedule) getApplicationContext());

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        //Schedulehelper
        scheduleHelper = new ScheduleHelper();

        //Search field
        editText = (EditText) findViewById(R.id.search_schedule);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    getSearchedSchedule();
                    return true;
                }
                return false;
            }
        });


        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add(getResources().getString(R.string.ddl_course));
        categories.add(getResources().getString(R.string.ddl_locale));
        categories.add(getResources().getString(R.string.ddl_programme));
        categories.add(getResources().getString(R.string.ddl_signature));


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        //TESTTESTTEST
        searchType = 0;
    }


    //Creates additional items in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        //menu.removeItem(R.id.action_refresh);
        //menu.removeItem(R.id.action_search);

        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_favourite).setVisible(false);
        menu.findItem(R.id.action_delete).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                GoToPreferences();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchType = position;
        buildSearchString();
        System.out.println("Position: " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void GoToPreferences() {
        Intent intent = new Intent(this, PrefActivity.class);
        startActivity(intent);
    }

    public String buildSearchString() {
        try {

            System.out.println(editText.getText().toString());

            String code = editText.getText().toString();
            code = code.replace("å", "%C3%A5");
            code = code.replace("Å", "%C3%A5");
            code = code.replace("ä", "%C3%A4");
            code = code.replace("Ä", "%C3%A4");
            code = code.replace("ö", "%C3%B6");
            code = code.replace("Ö", "%C3%B6");

            System.out.println(code);

            String defaultSearchString = getResources().getString(R.string.default_search_string);
            String defaultSearchString2 = getResources().getString(R.string.default_search_string2);
            String searchLanguage = getResources().getString(R.string.default_search_string_language);
            String type;

            if (searchType == 0) {
                type = getResources().getString(R.string.default_search_string_course);
            } else if (searchType == 1) {
                type = getResources().getString(R.string.default_search_string_room);
            } else if (searchType == 2) {
                type = getResources().getString(R.string.default_search_string_programme);
            } else {
                type = getResources().getString(R.string.default_search_string_signature);
            }

            String completeSearchString = defaultSearchString + searchLanguage + defaultSearchString2
                    + type + code;

            System.out.println(completeSearchString);

            return completeSearchString;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
        }
        return null;
    }

    public void getSearchedSchedule() {
        Schedule mSchedule = scheduleHelper.FetchSchedule(buildSearchString());
        if (checkCorrectSchedule(mSchedule)) {
            globalSchedule.setUrl(mSchedule.getUrl());
            globalSchedule.setType(searchType + 1);
            globalSchedule.setDocument(mSchedule.getDocument());
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(SearchActivity.this, getResources().getString(R.string.search_string_incorrect), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkCorrectSchedule(Schedule schedule) {
        try {
            //ToDo Make less worthless validation
            schedule.getDocument().select("td.big2 > table > tbody > tr > td").first().text();
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
            return false;
        }
    }
}