package myschedule.myschedule.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent mIntent = new Intent(context, UpdateService.class);
        context.startService(mIntent);
    }
}
