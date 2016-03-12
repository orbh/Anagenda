package myschedule.myschedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.nodes.Element;

import java.util.List;


public class SavedScheduleAdapter extends ArrayAdapter<Element>{
    SavedScheduleAdapter(Context context, List<Element> elements) {
        super(context, R.layout.saved_schedules_layout);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //ToDo Look up ViewHolder to speed up the ListView
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.saved_schedules_layout, parent, false);

        Element element = getItem(position);
        ImageView icon = (ImageView)customView.findViewById(R.id.saved_schedules_icon);
        TextView description = (TextView)customView.findViewById(R.id.saved_schedules_description);
        TextView next_event = (TextView)customView.findViewById(R.id.saved_schedules_next_event);
        TextView coursecode = (TextView)customView.findViewById(R.id.saved_schedules_coursecode);



        return customView;
    }

}
