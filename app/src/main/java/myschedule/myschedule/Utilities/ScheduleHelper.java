package myschedule.myschedule.Utilities;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.concurrent.ExecutionException;

import myschedule.myschedule.Objects.Schedule;
import myschedule.myschedule.Objects.ScheduleFile;
import myschedule.myschedule.Utilities.AsyncKronoXHelper;

public class ScheduleHelper {

    //Fetches new schedule fron KronoX
    public Schedule FetchSchedule(String url) {
        try {
            Schedule newSchedule = new Schedule();
            newSchedule.setDocument(new AsyncKronoXHelper().execute(url).get());
            //newSchedule.setType(getScheduleType(newSchedule));
            newSchedule.setUrl(url);
            return newSchedule;

        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("InterruptedException", "InterruptedException" + e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("ExecutionException", "ExecutionExeption" + e);
        }
        return null;
    }


    //Fetches the kind of schedule
    public Integer getScheduleType(Schedule schedule) {

        try {
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
        catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
            return 0;
        }
    }

    public File[] getSavedSchedules(Context c) {
        return c.getFilesDir().listFiles();
    }

    public void updateAllSchedules (File[] fileList, Context context) {
        for (File file : fileList) {
            updateSchedule(file, file.getPath(), context);
        }
    }

    public void updateSchedule(File f, String path, Context c) {
        try {
            ScheduleFile scheduleFile;
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            scheduleFile = (ScheduleFile) ois.readObject();
            ois.close();

            ScheduleHelper scheduleHelper = new ScheduleHelper();
            Schedule newSchedule = scheduleHelper.FetchSchedule(scheduleFile.getUrl());

            //ToDo Implement method for checking for updates

            File deletedFile = new File(path);
            deletedFile.delete();

            ScheduleFile saveSchedule = new ScheduleFile();
            saveSchedule.setUrl(newSchedule.getUrl());
            saveSchedule.setType(newSchedule.getType());
            saveSchedule.setSchedule(newSchedule.getDocument().toString());

            FileOutputStream fos = c.openFileOutput(f.getName(), c.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(saveSchedule);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", "FileNotFoundException" + e);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            Log.e("StreamCorruptedE", "StreamCorruptedE" + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "IOException" + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("ClassNotFoundException", "ClassNotFoundException" + e);
        }
    }

}
