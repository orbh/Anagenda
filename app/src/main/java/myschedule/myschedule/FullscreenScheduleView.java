package myschedule.myschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FullscreenScheduleView extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_schedule_layout);
    }

    public void ChangeActivity(View v){
        Intent intent = new Intent(this, MySchedules.class);
        startActivity(intent);
    }
}