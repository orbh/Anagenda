package myschedule.myschedule;

import org.jsoup.nodes.Document;

public class Schedule {

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