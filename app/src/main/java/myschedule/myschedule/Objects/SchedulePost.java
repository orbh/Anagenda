package myschedule.myschedule.Objects;

import java.io.Serializable;

public class SchedulePost implements Serializable {

    public String description;
    public String time;
    public String weekday;
    public String date;
    public String locale;
    public int type;
    public String course;
    public String group;
    public String lastUpdated;
    public String signature;
    public boolean changed;
    public String ChangeDate;

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getChangeDate() {
        return ChangeDate;
    }

    public void setChangeDate(String changeDate) {
        ChangeDate = changeDate;
    }

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

    public void setType(int type) {
        this.type = type;
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

    public int getType() {
        return type;
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
