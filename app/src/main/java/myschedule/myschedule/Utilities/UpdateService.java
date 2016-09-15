package myschedule.myschedule.Utilities;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import myschedule.myschedule.Objects.Schedule;

public class UpdateService extends IntentService {

    public UpdateService() {
        super("UpdateService");
    }

    public void onCreate() {
    super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //TO BE ACTIVATED WHEN UPDATE IS FINISHED
        /*
        Context context = getBaseContext();
        ScheduleHelper scheduleHelper = new ScheduleHelper();
        List<Schedule> scheduleList = scheduleHelper.LoadAllSchedules(context);
        scheduleHelper.updateAllSchedules(scheduleHelper.getSavedSchedules(c), c);
        */
    }
}
