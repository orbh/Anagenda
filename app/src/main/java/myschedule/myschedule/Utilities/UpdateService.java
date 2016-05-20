package myschedule.myschedule.Utilities;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UpdateService extends IntentService {

    public UpdateService() {
        super("UpdateService");
    }

    public void onCreate() {
    super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        System.out.println("UpdateService: onHandleIntent() triggered!");
        Context c = getBaseContext();
        ScheduleHelper scheduleHelper = new ScheduleHelper();
        scheduleHelper.getSavedSchedules(c);
        scheduleHelper.updateAllSchedules(scheduleHelper.getSavedSchedules(c), c);

        System.out.println("Updated All Schedules!");
    }
}
