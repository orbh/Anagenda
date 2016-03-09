package myschedule.myschedule;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MySchedules extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedules);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void ChangeActivityToFullscreen(View v) {
        Intent intent = new Intent(this, FullscreenScheduleView.class);
        startActivity(intent);
    }

    public void ChangeActivityToTestclass(View v) {
        Intent intent = new Intent(this, TESTKLASS.class);
        startActivity(intent);
    }
}