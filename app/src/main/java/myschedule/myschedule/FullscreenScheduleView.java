package myschedule.myschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FullscreenScheduleView extends Activity{

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