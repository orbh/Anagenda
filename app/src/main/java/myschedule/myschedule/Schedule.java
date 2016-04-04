package myschedule.myschedule;

import android.app.Application;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;

public class Schedule extends Application {

    public String url;
    public Document schedule;
    public int type;



    public String getUrl() {
        return url;
    }

    public void setUrl(String string) {
        url = string;
    }

    public Document getDocument() {
        return schedule;
    }

    public void setDocument(Document document) {
        schedule = document;
    }

    public int getType() { return type; }

    public void setType(int number) { type = number; }

    public void setSchedule(String link, Document doc, Integer number) {
        url = link;
        schedule = doc;
        type = number;
    }

}