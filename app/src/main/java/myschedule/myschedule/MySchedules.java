package myschedule.myschedule;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MySchedules extends AppCompatActivity {

    //Toolbar
    Toolbar toolbar;

    //Adapter for saved schedules-list
    SavedScheduleAdapter savedScheduleAdapter;

    //Stuff to do with the list with saved schedules
    ListView savedScheduleListView;
    List<Document> documentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedules);

        //Toolbar
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_title_myschedules));

        //ListView + adapter = true
        savedScheduleListView = (ListView) findViewById(R.id.listview_saved_schedules);
        savedScheduleAdapter = new SavedScheduleAdapter(this, documentList);
        savedScheduleListView.setAdapter(savedScheduleAdapter);

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

    //Loads saved schedules from files
    public void LoadSavedSchedules() {
        //ToDo Connect to saved list of schedules

        //Fetches all files in the files directory
        File childFile[] = getFilesDir().listFiles();

        //Tries to make the files into documents again and push them into the documentList
        for (File file: childFile) {

            File testFile = new File(file.getPath());
            String emptyUri = "";

            try {
                Document document = Jsoup.parse(testFile, "UTF-8", emptyUri);
                documentList.add(document);
                System.out.println(documentList.size());
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.e("IOEXception", "IOException"+e);
            }

        }

    }

}