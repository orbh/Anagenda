package myschedule.myschedule.Utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.WriteAbortedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import myschedule.myschedule.Activities.ScheduleActivity;
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
            FileOutputStream fos = context.openFileOutput(activeSchedule.getCode() + ".SCHEDULE", context.MODE_PRIVATE);
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
            if (!file.isDirectory()) {
                Schedule listSchedule = FileIntoSchedule(file, context);
                //Doesn't add schedule if it fails to fully turn into schedule
                if (listSchedule != null) {
                    scheduleList.add(listSchedule);
                }
                //Deletes the file if it's a non-correct file
                //ToDo Maybe this isn't the best solution
                else {
                    DeleteSchedule(file.getName(), context);
                }
            }
        }
        return scheduleList;
    }

    // Deletes the currently active schedule
    public void DeleteSchedule(String name, Context context) {
        try {
            //TESTING
            System.out.println("NAME: " + name);

            for (File f : LoadFileArray(context))
                if (f.getName().equals(name)) {
                    f.delete();
                    System.out.println("DELETEED: " + name);
                }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("IndexOutOfBoundsEx", "IndexOutOfBoundsEx" + e);
        }
    }

    public Schedule FileIntoSchedule(File file, Context context) {
        try {
            if (!file.isDirectory()) {
                FileInputStream fin = new FileInputStream(file.getPath());
                ObjectInputStream ois = new ObjectInputStream(fin);
                return (Schedule) ois.readObject();
            } else return null;

        } catch (IOException e) {
            //Deletes file if it fails to convert
            DeleteSchedule(file.getName(), context);
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
        return context.getFilesDir().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory() && pathname.getName().endsWith(".SCHEDULE");
            }
        });
    }

    public void UpdateAllSchedules(Context context) {
        for (Schedule schedule : LoadAllSchedules(context)) {
            UpdateSchedule(schedule, context);
        }
    }

    public void UpdateSchedule(Schedule savedSchedule, Context context) {

        /*
        boolean changed = false;
        boolean updated = false;
        boolean sameSize = false;
        */

        try {
            Schedule newSchedule = FetchSchedule(savedSchedule.getUrl());
            List<SchedulePost> changedPosts = new ArrayList<>();
            List<SchedulePost> updatedPosts = new ArrayList<>();

            //Creates new list of schedulepost without passed dates
            List<SchedulePost> trimmedSavedList = TrimPostList(savedSchedule.getPostList(), context);

            //Checks for posts with a later update-date than when the saved schedule was updated
            //This catches all kind of updates to a post that was done later than a day before
            Date savedScheduleDate = GetDateFormat().parse(savedSchedule.getLastUpdated());
            for (SchedulePost schedulePost : newSchedule.getPostList()) {
                Date postDate = GetDateFormat().parse(schedulePost.getLastUpdated());
                if (postDate.after(savedScheduleDate)) {
                    schedulePost.setChanged(true);
                    changedPosts.add(schedulePost);
                    //changed = true;
                }
            }

            /*

            //Tries to catch updated posts
            if (savedSchedule.getPostList().size() == trimmedSavedList.size())
            {
                sameSize = true;

                //Cross-references all scheduleposts to look for irregularities and
                //adds them to changedPosts. This should catch updates to existing posts.
                for (int i = 0; i < savedSchedule.getPostList().size(); i++) {
                    newSchedule.getPostList().get(i)
                            .setChanged(CompareSchedulePost(savedSchedule.getPostList()
                                    .get(i), newSchedule.getPostList().get(i), context));

                    if (newSchedule.getPostList().get(i).isChanged()) {
                        updatedPosts.add(newSchedule.getPostList().get(i));
                        updated = true;
                    }
                }
            }

            */

            //If there have been found some changed posts
            if (!changedPosts.isEmpty()) {

                //Prepares the global schedule
                Schedule globalSchedule = (Schedule) context.getApplicationContext();
                globalSchedule.setPostList(newSchedule.getPostList());
                globalSchedule.setLastUpdated(newSchedule.getLastUpdated());
                globalSchedule.setCode(newSchedule.getCode());
                globalSchedule.setName(newSchedule.getName());
                globalSchedule.setUrl(newSchedule.getUrl());
                globalSchedule.setType(newSchedule.getType());
                globalSchedule.setListIndex(newSchedule.getListIndex());

                Schedule updatedSchedule = new Schedule();
                updatedSchedule.setPostList(newSchedule.getPostList());
                updatedSchedule.setLastUpdated(newSchedule.getLastUpdated());
                updatedSchedule.setCode(newSchedule.getCode());
                updatedSchedule.setName(newSchedule.getName());
                updatedSchedule.setUrl(newSchedule.getUrl());
                updatedSchedule.setType(newSchedule.getType());
                updatedSchedule.setListIndex(newSchedule.getListIndex());

                DeleteSchedule(savedSchedule.getName(), context);
                SaveSchedule(updatedSchedule, context);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(R.drawable.emptystate3);
                builder.setContentTitle(savedSchedule.getName() + " has been changed");
                builder.setContentText(String.valueOf(changedPosts.size()) + " posts have been updated.");

                Intent resultIntent = new Intent(context, ScheduleActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(resultPendingIntent);
                int notificationId = 001;
                NotificationManager notiManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                notiManager.notify(notificationId, builder.build());
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ParseException", "ParseException" + e);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
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
            Iterator<SchedulePost> iter = postList.iterator();

            while (iter.hasNext()) {
                SchedulePost schedulePost = iter.next();

                Date strDate = GetDateFormat().parse(ConvertToFullDate(schedulePost.getDate()));
                if (new Date().after(strDate))
                    iter.remove();
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
        String lastPostDate = "";
        String lastWeekDay = "";

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

            //Sets the startdate
            String startDate = doc.select("td.data").get(2).text();
            String[] splitDate = startDate.split("\\s+");
            String formattedStartDate = splitDate[1];

            createdSchedule.setStartDate(formattedStartDate);
            createdSchedule.setUrl(url);
            createdSchedule.setPostList(postList);
            createdSchedule.setLastUpdated(GetDateFormat().format(new Date()));

            Elements elements = doc.select("table.schemaTabell > tbody > tr.data-white, tr.data-grey");
            ArrayList<Element> elementList = new ArrayList<>();

            for (Element element : elements) {
                elementList.add(element);
            }

            for (Element element : elementList) {

                SchedulePost toListPost = new SchedulePost();

                //Course
                if (createdSchedule.getType() == 1) {

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
                }

                //Room
                else if (createdSchedule.getType() == 2) {

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
                }

                //Programme
                else if (createdSchedule.getType() == 3) {

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
                }

                //Signature
                else if (createdSchedule.getType() == 4) {

                    toListPost.setDate(element.child(2).text());
                    toListPost.setDescription(element.child(8).text());
                    toListPost.setTime(element.child(3).text());
                    toListPost.setLocale(element.child(6).text());
                    toListPost.setCourse(element.child(5).text());
                    toListPost.setLastUpdated(element.child(9).text());
                    toListPost.setWeekday(element.child(1).text());

                    createdSchedule.setName(splitTitle.get(1));
                    createdSchedule.setCode(splitTitle.get(0));
                }

                if (!(toListPost.getDate().trim().length() > 0)) {
                    toListPost.setDate(lastPostDate);
                    toListPost.setWeekday(lastWeekDay);
                }

                postList.add(toListPost);
                lastPostDate = toListPost.getDate();
                lastWeekDay = toListPost.getWeekday();
            }

            return createdSchedule;

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
            return null;
        }
    }

    public SimpleDateFormat GetDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public String ConvertToFullDate(String input) {

        try {
            if(!input.equals(" ") || !input.equals("")) {
                String[] splitString = input.split("\\s+");

                Map<String, String> map = new HashMap<>();
                //English
                map.put("Jan", "01");
                map.put("Feb", "02");
                map.put("Mar", "03");
                map.put("Apr", "04");
                map.put("May", "05");
                map.put("Jun", "06");
                map.put("Jul", "07");
                map.put("Aug", "08");
                map.put("Sep", "09");
                map.put("Oct", "10");
                map.put("Nov", "11");
                map.put("Dec", "12");

                //Swedish
                map.put("Maj", "05");
                map.put("Okt", "10");

                String date = splitString[0];
                if (date.length() == 1) {
                    date = "0" + date;
                }

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                return year + "-" + map.get(splitString[1]) + "-" + date;
            }
            else {
                return "";
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("ArrayIndexOutOfBounds", "ArrayIndexOutOfBounds" + e);
            return "";
        }
    }

    public String ConvertFromFullDate(String input) {
        String[] splitString = input.split("-");
        Map<String, String> map = new HashMap<>();
        map.put("01", "jan");
        map.put("02", "feb");
        map.put("03", "mar");
        map.put("04", "apr");
        map.put("05", "may");
        map.put("06", "jun");
        map.put("07", "jul");
        map.put("08", "aug");
        map.put("09", "sep");
        map.put("10", "oct");
        map.put("11", "nov");
        map.put("12", "jan");

        return splitString[2] + " " + map.get(splitString[1]);
    }

}

