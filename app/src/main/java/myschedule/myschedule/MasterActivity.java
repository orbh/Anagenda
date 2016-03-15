package myschedule.myschedule;

import android.support.v7.app.AppCompatActivity;

import org.jsoup.nodes.Document;

public class MasterActivity extends AppCompatActivity{

    public Schedule loadedSchedule;

    public String getUrl()
    { return loadedSchedule.getUrl(); }

    public void setUrl(String string) {
        loadedSchedule.setUrl(string);
    }

    public Document getDocument(){
        return loadedSchedule.getDocument();
    }

    public void setDocument(Document document){
        loadedSchedule.setDocument(document);
    }

}
