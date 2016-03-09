package myschedule.myschedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jsoup.nodes.Element;

import java.util.List;

public class CustomScheduleAdapter extends ArrayAdapter<Element>{

    CustomScheduleAdapter(Context context, List<Element> elements) {
        super(context, R.layout.schedule_layout, elements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //ToDo Look up ViewHolder to speed up the ListView

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.schedule_layout, parent, false);

        Element element = getItem(position);
        TextView description = (TextView) customView.findViewById(R.id.description_textview);
        TextView weekday = (TextView) customView.findViewById(R.id.weekday_textview);
        TextView date = (TextView) customView.findViewById(R.id.date_textview);

        description.setText(element.child(9).text());
        weekday.setText(element.child(1).text());
        date.setText(element.child(2).text());
        return customView;
    }

}
