package myschedule.myschedule.Utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.WriteAbortedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import myschedule.myschedule.Activities.HomeActivity;
import myschedule.myschedule.Objects.Schedule;
import myschedule.myschedule.Objects.SchedulePost;
import myschedule.myschedule.R;

public class ScheduleHelper {

    //Fetches new schedule from KronoX
    public Schedule FetchSchedule(String url) {
        try {
            return buildSchedule(new AsyncKronoXHelper().execute(url).get(), url);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("InterruptedException", "InterruptedException" + e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("ExecutionException", "ExecutionExeption" + e);
        }
        return null;
    }

    //Attempts to save schedule
    public void SaveSchedule(Schedule activeSchedule, Context context) {

        try {

            FileOutputStream fos = context.openFileOutput(activeSchedule.getCode(), context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(activeSchedule);
            oos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", "FileNotFoundException" + e);
        } catch (WriteAbortedException e) {
            e.printStackTrace();
            Log.e("WriteAbortedException", "WriteAbortedException" + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "IOException" + e);
        }
    }

    public ArrayList<Schedule> LoadAllSchedules(Context context) {
        ArrayList<Schedule> scheduleList = new ArrayList<>();
        for (File file : LoadFileArray(context)) {
            Schedule listSchedule = FileIntoSchedule(file, context);
            scheduleList.add(listSchedule);
        }
        return scheduleList;
    }

    // Deletes the currently active schedule
    public void DeleteSchedule(Schedule schedule, Context context) {

        try {
            File files[] = context.getFilesDir().listFiles();
            for (File f : files)
                if (f.getName().equals(schedule.getCode()) || f.getName().equals(schedule.getName())) {
                    f.delete();
                    return;
                }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("IndexOutOfBoundsEx", "IndexOutOfBoundsEx" + e);
        }
    }

    public Schedule FileIntoSchedule(File file, Context context) {
        try {
            FileInputStream fin = new FileInputStream(file.getPath());
            ObjectInputStream ois = new ObjectInputStream(fin);
            return (Schedule) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "IOException" + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("ClassNotFoundException", "ClassNotFoundException" + e);
        }

        return null;

    }

    public Schedule LoadSavedSchedule(String code, Context context) {
        for (File file : LoadFileArray(context)) {
            if (file.getName().equals(code)) {
                return FileIntoSchedule(file, context);
            }
        }
        return null;
    }

    public File[] LoadFileArray(Context context) {
        return context.getFilesDir().listFiles();
    }

    public void UpdateSchedule(Schedule savedSchedule, Context context) {

        //First makes sure that both schedules contain the same amount of posts
        savedSchedule.setPostList(TrimPostList(savedSchedule.getPostList(), context));
        Schedule newSchedule = FetchSchedule(savedSchedule.getUrl());

        if (savedSchedule.getPostList().size() == newSchedule.getPostList().size()) {
            for (int i = 0; i < savedSchedule.getPostList().size(); i++) {
                newSchedule.getPostList().get(i)
                        .setChanged(CompareSchedulePost(savedSchedule.getPostList()
                                .get(i), newSchedule.getPostList().get(i), context));
            }
        }
    }

    //Compares both
    public boolean CompareSchedulePost(SchedulePost oldSchedulePost, SchedulePost newSchedulePost, Context context) {

        if (oldSchedulePost.getDescription().equals(newSchedulePost.getDescription())) {
            return true;
        }
        if (oldSchedulePost.getTime().equals(newSchedulePost.getTime())) {
            return true;
        }
        if (oldSchedulePost.getWdAndDate().equals(newSchedulePost.getWdAndDate())) {
            return true;
        }
        if (oldSchedulePost.getLocale().equals(newSchedulePost.getLocale())) {
            return true;
        }
        if (oldSchedulePost.getType() == (newSchedulePost.getType())) {
            return true;
        }
        if (oldSchedulePost.getCourse().equals(newSchedulePost.getCourse())) {
            return true;
        }
        if (oldSchedulePost.getGroup().equals(newSchedulePost.getGroup())) {
            return true;
        }
        if (oldSchedulePost.getLastUpdated().equals(newSchedulePost.getLastUpdated())) {
            return true;
        }
        if (oldSchedulePost.getSignature().equals(newSchedulePost.getSignature())) {
            return true;
        }
        return false;
    }

    public List<SchedulePost> TrimPostList(List<SchedulePost> postList, Context context) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (SchedulePost schedulePost : postList) {
                Date strDate = sdf.parse(schedulePost.getDate());
                if (new Date().after(strDate)) {
                    postList.remove(schedulePost);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ParseException", "ParseException" + e);
        }
        return postList;
    }

    //Converts the Jsoup document into a schedule
    public Schedule buildSchedule(Document doc, String url) {

        Schedule createdSchedule = new Schedule();

        //ToDo Should check localized strings instead
        //Sets type for schedule
        try {
            String type = doc.select("td.big2 > table > tbody > tr > td").first().text();
            if (type.contains("Kurs") || type.contains("Course")) {
                createdSchedule.setType(1);
            } else if (type.contains("Lokal") || type.contains("Room")) {
                createdSchedule.setType(2);
            } else if (type.contains("Program") || type.contains("Programme")) {
                createdSchedule.setType(3);
            } else if (type.contains("Signatur") || type.contains("Signature")) {
                createdSchedule.setType(4);
            } else {
                createdSchedule.setType(0);
            }

            ArrayList<SchedulePost> postList = new ArrayList<>();

            //Fetches schedule name
            String fullName = doc.select("td.big2 > table > tbody > tr > td").get(1).text();
            List<String> splitTitle = Arrays.asList(fullName.split(", "));

            createdSchedule.setUrl(url);
            createdSchedule.setPostList(postList);

            Elements elements = doc.select("table.schemaTabell > tbody > tr.data-white, tr.data-grey");
            ArrayList<Element> elementList = new ArrayList<>();

            for (Element element : elements) {
                elementList.add(element);
            }

            for (Element element : elementList) {

                //Course
                if (createdSchedule.getType() == 1) {
                    SchedulePost toListPost = new SchedulePost();
                    toListPost.setDescription(element.child(9).text());
                    toListPost.setDate(element.child(2).text());
                    toListPost.setLocale(element.child(7).text());
                    toListPost.setTime(element.child(3).text());
                    toListPost.setGroup(element.child(5).text());
                    toListPost.setLastUpdated(element.child(10).text());
                    toListPost.setSignature(element.child(6).text());
                    toListPost.setWeekday(element.child(1).text());

                    createdSchedule.setName(splitTitle.get(1));
                    createdSchedule.setCode(splitTitle.get(0));
                    postList.add(toListPost);
                }

                //Room
                else if (createdSchedule.getType() == 2) {
                    SchedulePost toListPost = new SchedulePost();
                    toListPost.setCourse(element.child(5).text());
                    toListPost.setDate(element.child(2).text());
                    toListPost.setDescription(element.child(9).text());
                    toListPost.setGroup(element.child(6).text());
                    toListPost.setTime(element.child(3).text());
                    toListPost.setSignature(element.child(7).text());
                    toListPost.setLastUpdated(element.child(10).text());
                    toListPost.setWeekday(element.child(1).text());

                    createdSchedule.setName(splitTitle.get(0));
                    createdSchedule.setCode(splitTitle.get(0));
                    postList.add(toListPost);
                }

                //Programme
                else if (createdSchedule.getType() == 3) {
                    SchedulePost toListPost = new SchedulePost();
                    toListPost.setDate(element.child(2).text());
                    toListPost.setSignature(element.child(5).text());
                    toListPost.setDescription(element.child(8).text());
                    toListPost.setTime(element.child(3).text());
                    toListPost.setLocale(element.child(6).text());
                    toListPost.setCourse(element.child(4).text());
                    toListPost.setLastUpdated(element.child(9).text());
                    toListPost.setWeekday(element.child(1).text());

                    createdSchedule.setName(splitTitle.get(0));
                    createdSchedule.setCode(splitTitle.get(0));
                    postList.add(toListPost);
                }

                //Signature
                else if (createdSchedule.getType() == 4) {
                    SchedulePost toListPost = new SchedulePost();
                    toListPost.setDate(element.child(2).text());
                    toListPost.setDescription(element.child(8).text());
                    toListPost.setTime(element.child(3).text());
                    toListPost.setLocale(element.child(6).text());
                    toListPost.setCourse(element.child(5).text());
                    toListPost.setLastUpdated(element.child(9).text());
                    toListPost.setWeekday(element.child(1).text());

                    createdSchedule.setName(splitTitle.get(1));
                    createdSchedule.setCode(splitTitle.get(0));
                    postList.add(toListPost);
                }
            }

            return createdSchedule;

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
            return null;
        }

    }

}
