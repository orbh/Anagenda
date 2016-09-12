package myschedule.myschedule.Objects;

public class SchedulePost {

    public String description;
    public String time;
    public String weekday;
    public String date;
    public String locale;

    public String course;
    public String group;
    public String lastUpdated;
    public String signature;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getDate() {
        return date;
    }

    public String getLocale() {
        return locale;
    }

    public String getCourse() {
        return course;
    }

    public String getGroup() {
        return group;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getSignature() {
        return signature;
    }

    public String getWdAndDate() {
        return getWeekday() + ", " + getDate();
    }

    public void clearPost() {
        description = "";
        time = "";
        weekday = "";
        date = "";
        locale = "";
        course = "";
        group = "";
        lastUpdated = "";
        signature = "";
    }

}
