package myschedule.myschedule;

import android.app.Application;

import org.jsoup.nodes.Document;

public class Schedule extends Application{

    public String url;
    public Document schedule;

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

}