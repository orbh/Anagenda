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
        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        globalSchedule = ((Schedule) getApplicationContext());
        scheduleHelper = new ScheduleHelper();

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        textViewInfo = (TextView) findViewById(R.id.searchinfo);
        editText = (EditText) findViewById(R.id.search_schedule);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    GetSearchedSchedule();
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void GoToPreferences() {
        Intent intent = new Intent(this, PrefActivity.class);
        startActivity(intent);
    }

    //ToDo Make this into a separate string class perhaps?
    public String buildSearchString() {
        try {
            String code = editText.getText().toString();
            code = code.replace("å", "%C3%A5");
            code = code.replace("Å", "%C3%A5");
            code = code.replace("ä", "%C3%A4");
            code = code.replace("Ä", "%C3%A4");
            code = code.replace("ö", "%C3%B6");
            code = code.replace("Ö", "%C3%B6");

            String defaultSearchString = getResources().getString(R.string.default_search_string);
            String defaultSearchString2 = getResources().getString(R.string.default_search_string2);
            String searchLanguage = getResources().getString(R.string.default_search_string_language);
            String type;

            if (searchType == 0) {
                type = getResources().getString(R.string.default_search_string_course);
                if (!code.endsWith("-")) {
                    code = code + "-";
                }
            } else if (searchType == 1) {
                type = getResources().getString(R.string.default_search_string_room);
            } else if (searchType == 2) {
                type = getResources().getString(R.string.default_search_string_programme);
            } else {
                type = getResources().getString(R.string.default_search_string_signature);
            }

            return defaultSearchString + searchLanguage + defaultSearchString2 + type + code;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
        }
        return null;
    }

    public void GetSearchedSchedule() {
        Schedule mSchedule = scheduleHelper.FetchSchedule(buildSearchString());

        if (mSchedule == null) {
            Toast.makeText(SearchActivity.this, getResources()
                    .getString(R.string.search_string_incorrect), Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            globalSchedule.setUrl(mSchedule.getUrl());
            globalSchedule.setType(mSchedule.getType());
            globalSchedule.setPostList(mSchedule.getPostList());
            globalSchedule.setCode(mSchedule.getCode());
            globalSchedule.setName(mSchedule.getName());
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
            Toast.makeText(SearchActivity.this, getResources()
                    .getString(R.string.search_string_incorrect), Toast.LENGTH_SHORT).show();
        }


    }
}
