package myschedule.myschedule.Utilities;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class UpdateService extends IntentService {

    public UpdateService() {
        super("UpdateService");
    }

    public void onCreate() {
    super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Context c = getBaseContext();
        ScheduleHelper scheduleHelper = new ScheduleHelper();
        scheduleHelper.getSavedSchedules(c);
        scheduleHelper.updateAllSchedules(scheduleHelper.getSavedSchedules(c), c);
    }
}
