package myschedule.myschedule.Objects;

import org.jsoup.nodes.Document;

import java.io.Serializable;

public class ScheduleFile implements Serializable {

    public String url;
    public String schedule;
    public int type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String string) {
        url = string;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String documentString) {
        schedule = documentString;
    }

    public int getType() {
        return type;
    }

    public void setType(int number) {
        type = number;
    }

}
