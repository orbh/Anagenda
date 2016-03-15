package myschedule.myschedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;


public class SavedScheduleAdapter extends ArrayAdapter<Schedule> {
    SavedScheduleAdapter(Context context, List<Schedule> documents) {
        super(context, R.layout.saved_schedules_layout, documents);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //ToDo Look up ViewHolder to speed up the ListView
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.saved_schedules_layout, parent, false);

        Schedule schedule = getItem(position);
        ImageView icon = (ImageView) customView.findViewById(R.id.saved_schedules_icon);
        TextView coursename = (TextView) customView.findViewById(R.id.saved_schedules_coursename);
        TextView next_event = (TextView) customView.findViewById(R.id.saved_schedules_next_event);
        TextView coursecode = (TextView) customView.findViewById(R.id.saved_schedules_coursecode);

        //Coursename and coursecode
        Element title = schedule.getDocument().select("td.big2 > table > tbody > tr > td").get(1);
        String wholeTitle = title.text();
        String[] splitTitle = wholeTitle.split("\\s*,\\s*");
        coursecode.setText(splitTitle[0]);
        coursename.setText(splitTitle[1] + ", " + splitTitle[2]);

        //Next event
        Element nextEvent = schedule.getDocument().select("table.schemaTabell > tbody > tr.data-white, tr.data-grey").first();
        String mergedWdAndDate = nextEvent.child(1).text() + ", " + nextEvent.child(2).text();
        String time = nextEvent.child(3).text();
        //ToDo Should be in strings.xml
        next_event.setText(getContext().getString(R.string.saved_schedules_next_event) + " " + mergedWdAndDate + " " + time);

        return customView;
    }

}
