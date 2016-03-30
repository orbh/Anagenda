package myschedule.myschedule;

import android.util.Log;

import java.util.concurrent.ExecutionException;

public class ScheduleHelper {

    //Fetches new schedule fron KronoX
    public Schedule FetchSchedule(String url) {
        try {
            Schedule newSchedule = new Schedule();
            newSchedule.setDocument(new AsyncKronoXHelper().execute(url).get());
            newSchedule.setType(getScheduleType(newSchedule));
            newSchedule.setUrl(url);
            return newSchedule;

        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("InterruptedException", "InterruptedException" + e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("ExecutionException", "ExecutionExeption" + e);
        }
        //ToDo Maybe null has to be something else
        return null;
    }

    //Fetches the kind of schedule
    public Integer getScheduleType(Schedule schedule) {

        String type = schedule.getDocument().select("td.big2 > table > tbody > tr > td").first().text();
        if(type.contains("Kurs") || type.contains("Course")){
            return 1;
        }
        else if (type.contains("Lokal") || type.contains("Room")){
            return 2;
        }

        else if (type.contains("Program") || type.contains("Programme")) {
            return 3;
        }

        else if (type.contains("Signatur") || type.contains("Signature")){
            return 4;
        }

        else {
            return 0;
        }
    }

}
