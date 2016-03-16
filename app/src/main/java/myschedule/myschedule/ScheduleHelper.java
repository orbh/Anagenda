package myschedule.myschedule;

public class ScheduleHelper {

    //Fetches the kind of schedule
    public Integer getScheduleType(Schedule schedule) {

        String type = schedule.getDocument().select("td.big2 > table > tbody > tr > td").first().text();
        if(type.contains("Kurs") || type.contains("Course")){
            return 1;
        }
        else if (type.contains("Lokal") || type.contains("Room")){
            return 2;
        }
        else if (type.contains("Signatur") || type.contains("Signature")){
            return 3;
        }
        else {
            return 0;
        }
    }

}
