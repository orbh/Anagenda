package myschedule.myschedule.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import myschedule.myschedule.R;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Spinner spinner;
    TextView textView;
    TextView textViewInfo;
    EditText editText;

    Integer searchType;

    //EditText editText1;
    //EditText editText2;
    //EditText editText3;
    //EditText editText4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewInfo = (TextView) findViewById(R.id.searchinfo);

        textView = (TextView) findViewById(R.id.searchText);
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        //Search field
        editText = (EditText) findViewById(R.id.search_schedule);


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Course");
        categories.add("Programme");
        categories.add("Signature");
        categories.add("Locale");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

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
        menu.removeItem(R.id.action_refresh);
        menu.removeItem(R.id.action_search);

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
            String code = "jpn";
            String defaultSearchString = getResources().getString(R.string.default_search_string);
            String defaultSearchString2 = getResources().getString(R.string.default_search_string2);
            String searchLanguage = getResources().getString(R.string.default_search_string_language);
            String type = "";

            if (searchType == 0) {
                type = getResources().getString(R.string.default_search_string_course);
            } else if (searchType == 1) {
                type = getResources().getString(R.string.default_search_string_programme);
            } else if (searchType == 2) {
                type = getResources().getString(R.string.default_search_string_signature);
            } else {
                type = getResources().getString(R.string.default_search_string_room);
            }

            String completeSearchString = defaultSearchString + searchLanguage + defaultSearchString2
                    + type + code;

            System.out.println(completeSearchString);

            return completeSearchString;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
        }
        return null;
    }

    public void getSearchedSchedule() {

    }

    public boolean checkForSchedule() {
        return true;
    }


}
