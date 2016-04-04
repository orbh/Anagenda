package myschedule.myschedule;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;


public class SavedScheduleAdapter extends RecyclerView.Adapter<SavedScheduleAdapter.ViewHolder> {

    private List<Schedule> sDataset;
    private Schedule schedule;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public LinearLayout rowlayout;

        public ImageView scheduleIcon;
        public TextView titel;
        public TextView text;
        public TextView course;

        public ViewHolder(View v) {
            super(v);

            cardView = (CardView) v.findViewById(R.id.schedule_card_view);
            rowlayout = (LinearLayout) v.findViewById(R.id.saved_schedules_row_layout);
            scheduleIcon = (ImageView) v.findViewById(R.id.saved_schedules_icon);
            titel = (TextView) v.findViewById(R.id.saved_schedules_coursename);
            text = (TextView) v.findViewById(R.id.saved_schedules_next_event);
            course = (TextView) v.findViewById(R.id.saved_schedules_coursecode);

        }
    }

    public SavedScheduleAdapter(List<Schedule>Dataset, Context context){

        sDataset = Dataset;
        schedule = ((Schedule) context.getApplicationContext());
    }

    @Override
    public SavedScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_schedules_layout, parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //Coursename and coursecode
    //    Element stitle = schedule.getDocument().select("td.big2 > table > tbody > tr > td").get(1);
        Element stitle = sDataset.get(position).getDocument().select("td.big2 > table > tbody > tr > td").get(1);
        String wholeTitle = stitle.text();
        String[] splitTitle = wholeTitle.split("\\s*,\\s*");

        //Next event
        Element nextEvent = schedule.getDocument().select("table.schemaTabell > tbody > tr.data-white, tr.data-grey").first();
        String mergedWdAndDate = nextEvent.child(1).text() + ", " + nextEvent.child(2).text();
        String time = nextEvent.child(3).text();
        //ToDo Should be in strings.xml
        //nextEvent.setText(getContext().getString(R.string.saved_schedules_next_event) + " " + mergedWdAndDate + " " + time);

        holder.cardView.setUseCompatPadding(true);
        holder.titel.setText("");
        holder.course.setText("");
        holder.text.setText("");


        Schedule schedule = sDataset.get(position);

        //Course
        if (schedule.getType() == 1) {
            holder.titel.setText(wholeTitle);
            holder.text.setText(mergedWdAndDate + time);
            holder.course.setText(splitTitle[0]);
            holder.scheduleIcon.setImageResource(R.drawable.ic_today_black_36dp);
        }
        //Room
        else if (schedule.getType() == 2) {

            holder.titel.setText(wholeTitle);
            holder.text.setText(mergedWdAndDate + time);
            holder.course.setText(splitTitle[1]);
            holder.scheduleIcon.setImageResource(R.drawable.ic_home_black_36dp);
        }
        //Person
        else if (schedule.getType() == 3) {
            holder.titel.setText(splitTitle[1]);
            holder.text.setText(mergedWdAndDate + time);
            holder.course.setText(splitTitle[0]);
            holder.scheduleIcon.setImageResource(R.drawable.ic_person_black_36dp);
        }
        }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sDataset.size();
    }
    }


