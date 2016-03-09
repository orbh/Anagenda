package myschedule.myschedule;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TESTKLASS extends AppCompatActivity {

    Toolbar toolbar;
    ListView lwSchedule;

    Drawer navigationDrawer;

    CustomScheduleAdapter customScheduleAdapter;

    //Contains all "rows" of posts
    List<Element> elementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testklass_layout);

        //Toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Navigation drawer
        PrimaryDrawerItem navMySchedules = new PrimaryDrawerItem().withName(R.string.navigation_drawer_my_schedules);
        PrimaryDrawerItem navFullscreenScheduleView = new PrimaryDrawerItem().withName(R.string.navigation_drawer_fullscreen_schedule_view);
        SecondaryDrawerItem navTestclass = new SecondaryDrawerItem().withName(R.string.navigation_drawer_testclass);
        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(navMySchedules,navFullscreenScheduleView, new DividerDrawerItem(),
                        navTestclass)
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {

                        //ToDo Code for pressing items in Nav Drawer below

                        return false;
                    }
                }).build();
        navigationDrawer.addStickyFooterItem(new PrimaryDrawerItem().withName(R.string.navigation_drawer_settings));

        //Schedule with custom adapter
        lwSchedule = (ListView)findViewById(R.id.lwSchedule);
        customScheduleAdapter = new CustomScheduleAdapter(this, elementList);
        lwSchedule.setAdapter(customScheduleAdapter);

        LoadSchedule();
    }

    public void LoadSchedule() {
        try {

            Document document = new AsyncHelper().execute(getResources().getString(R.string.default_schedule)).get();

            //Fetches table with only schedule rows
            Elements posts = document.select("table.schemaTabell > tbody > tr.data-white, tr.data-grey");

            //Puts each row into a list
            for (Element element : posts) {
                elementList.add(element);
            }

            //Sets the name of toolbar
            Element title = document.select("td.big2 > table > tbody > tr > td").get(1);
            String input = title.text();
            String output = input.substring(input.indexOf(",") + 1);
            assert getSupportActionBar() != null;
            if(output.equals("")){
                getSupportActionBar().setTitle("Schema");
            }
            else getSupportActionBar().setTitle(output);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("InterruptedException", "InterruptedException" + e);
        }
        catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("ExecutionException", "ExecutionExeption" + e);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerExeption", "NullPointerExeption" + e);
        }
    }

}