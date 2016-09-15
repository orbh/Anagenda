package myschedule.myschedule.Objects;

import android.app.Application;

import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.List;

public class Schedule extends Application implements Serializable {

    public String url;
    public int type;
    public List<SchedulePost> postList;
    public String code;
    public String name;

    public List<SchedulePost> getPostList() {
        return postList;
    }

    public void setPostList(List<SchedulePost> list) {
        postList = list;
    }

    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String string) {
        url = string;
    }

    public int getType() { return type; }

    public void setType(int number) { type = number; }
}