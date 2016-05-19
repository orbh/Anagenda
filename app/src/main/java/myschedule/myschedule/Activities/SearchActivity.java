package myschedule.myschedule.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import myschedule.myschedule.R;

/**
 * Created by Vivvisnurr on 2016-05-17.
 */
public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Spinner spinner;
    TextView textView;
    TextView textViewInfo;

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

        //editText1 = (EditText) findViewById(R.id.search_Course);
        //editText2 = (EditText) findViewById(R.id.search_Locale);
        //editText3 = (EditText) findViewById(R.id.search_Programme);
        //editText4 = (EditText) findViewById(R.id.search_Signature);




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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
