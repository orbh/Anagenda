package myschedule.myschedule;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class CustomScheduleAdapter extends ArrayAdapter<Element> {


    CustomScheduleAdapter(Context context, List<Element> elements) {
        super(context, R.layout.schedule_layout, elements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //ToDo Look up ViewHolder to speed up the ListView
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.schedule_layout, parent, false);

        try {


            Element element = getItem(position);
            RelativeLayout rowLayout = (RelativeLayout) customView.findViewById(R.id.row_layout);
            TextView description = (TextView) customView.findViewById(R.id.description_textview);
            TextView wdAndDate = (TextView) customView.findViewById(R.id.wd_and_date_textview);
            TextView locale = (TextView) customView.findViewById(R.id.locale_textview);
            TextView time = (TextView) customView.findViewById(R.id.time_textview);
            TextView course = (TextView) customView.findViewById(R.id.course_textview);
            ImageView color = (ImageView) customView.findViewById(R.id.row_color_imageview);

            description.setText("");
            wdAndDate.setText("");
            locale.setText("");
            time.setText("");
            course.setText("");
            course.setVisibility(View.GONE);

            //Makes half of the rows grayish and makes the color on the side
            if ((position % 2) == 0) {
                rowLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRowGray));
            }

            //Sets random color for side
            Random numberGenerator = new Random();
            int number = numberGenerator.nextInt(3);
            if (number == 0) {
                color.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            } else if (number == 1) {
                color.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            } else if (number == 2) {
                color.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
            }

            Schedule schedule = ((Schedule) getContext().getApplicationContext());

            //Merges weekday and date
            //ToDo Make the comma go away then weekday and date are missing
            String mergedWdAndDate = element.child(1).text() + ", " + element.child(2).text();
            wdAndDate.setText(mergedWdAndDate);


            //Course
            if (schedule.getType() == 1) {

                description.setText(element.child(9).text());
                locale.setText(element.child(7).text());
                time.setText(element.child(3).text());
                return customView;

            }
            //Room
            else if (schedule.getType() == 2) {

                description.setText(element.child(9).text());
                time.setText(element.child(3).text());
                locale.setText(element.child(5).text());
                return customView;

            }

            //Programme
            //ToDo Maybe set color as bigger
            else if (schedule.getType() == 3) {

                description.setText(element.child(8).text());
                time.setText(element.child(3).text());
                locale.setText(element.child(6).text());
                course.setVisibility(View.VISIBLE);
                course.setText(element.child(4).text());
                return customView;

            }

            //Signature
            else if (schedule.getType() == 4) {

                description.setText(element.child(9).text());
                time.setText(element.child(3).text());
                locale.setText(element.child(6).text());
                course.setText(element.child(5).text());
                return customView;
            }

        /*
        //Highlights posts with tenta or omtenta as locale
        String localeInfo = element.child(7).text();
        if (localeInfo.contains("Tentamen") || localeInfo.contains("Omtentamen")) {
            rowLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            description.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextPrimary));
            wdAndDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextPrimary));
            locale.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextPrimary));
            time.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextPrimary));
        }
        */

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
        }

        return customView;

    }
}