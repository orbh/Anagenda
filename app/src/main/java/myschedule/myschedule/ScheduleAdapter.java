package myschedule.myschedule;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.nodes.Element;
import org.w3c.dom.Text;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<Element> mDataset;
    private Schedule schedule;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public CardView cardView;

        public LinearLayout rowLayout;

        public RelativeLayout rowUpperLayout;
        public TextView description;
        public TextView time;
        public TextView wdAndDate;
        public TextView locale;
        public ImageView arrow;


        public RelativeLayout rowExtendedLayout;
        public TextView course;
        public TextView group;
        public TextView lastUpdated;

        public ViewHolder(View v) {
            super(v);

            cardView = (CardView) v.findViewById(R.id.schedule_card_view);

            rowLayout = (LinearLayout) v.findViewById(R.id.schedule_row);

            rowUpperLayout = (RelativeLayout) v.findViewById(R.id.schedule_upper_row_layout);
            description = (TextView) v.findViewById(R.id.description_textview);
            time = (TextView) v.findViewById(R.id.time_textview);
            wdAndDate = (TextView) v.findViewById(R.id.wd_and_date_textview);
            locale = (TextView) v.findViewById(R.id.locale_textview);
            arrow = (ImageView) v.findViewById(R.id.row_button_extend);


            rowExtendedLayout = (RelativeLayout) v.findViewById(R.id.schedule_lower_row_layout);
            course = (TextView) v.findViewById(R.id.course_textview);
            group = (TextView) v.findViewById(R.id.schedule_group);
            lastUpdated = (TextView) v.findViewById(R.id.schedule_last_updated);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(List<Element> myDataset, Context context) {
        mDataset = myDataset;
        schedule = ((Schedule) context.getApplicationContext());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_layout, parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.rowExtendedLayout.getVisibility() == View.GONE) {
                        holder.rowExtendedLayout.setVisibility(View.VISIBLE);
                        holder.arrow.setImageResource(R.drawable.ic_keyboard_arrow_up_grey_500_18dp);
                    } else {
                        holder.rowExtendedLayout.setVisibility(View.GONE);
                        holder.arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_grey_500_18dp);
                    }
                }
            });

            holder.cardView.setUseCompatPadding(true);

            holder.description.setText("");
            holder.wdAndDate.setText("");
            holder.locale.setText("");
            holder.time.setText("");
            holder.group.setText("");

            holder.rowExtendedLayout.setVisibility(View.GONE);
            holder.arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_grey_500_18dp);
            holder.course.setText("");
            holder.course.setVisibility(View.GONE);

            Element element = mDataset.get(position);

            //Merges weekday and date
            //ToDo Make the comma go away then weekday and date are missing
            String mergedWdAndDate = element.child(1).text() + ", " + element.child(2).text();
            holder.wdAndDate.setText(mergedWdAndDate);


            //Course
            if (schedule.getType() == 1) {

                holder.description.setText(element.child(9).text());
                holder.locale.setText(element.child(7).text());
                holder.time.setText(element.child(3).text());
                if (element.child(5).text().equals("")) {
                    holder.group.setVisibility(View.GONE);
                } else {
                    holder.group.setVisibility(View.VISIBLE);
                    holder.group.setText(R.string.schedule_group);
                }
                holder.lastUpdated.setText("Last updated:" + " " + element.child(10).text());

            }
            //Room
            else if (schedule.getType() == 2) {

                holder.description.setText(element.child(9).text());
                holder.time.setText(element.child(3).text());
                holder.locale.setText(element.child(7).text());
                if (element.child(6).text().equals("")) {
                    holder.group.setVisibility(View.GONE);
                } else {
                    holder.group.setVisibility(View.VISIBLE);
                    holder.group.setText(R.string.schedule_group + element.child(6).text());
                }
                holder.course.setText(element.child(5).text());
                holder.lastUpdated.setText("Last updated:" + " " + element.child(9).text());
            }

            //Programme
            //ToDo Maybe set color as bigger
            else if (schedule.getType() == 3) {

                holder.description.setText(element.child(8).text());
                holder.time.setText(element.child(3).text());
                holder.locale.setText(element.child(6).text());
                holder.course.setVisibility(View.VISIBLE);
                holder.course.setText(element.child(4).text());
                holder.lastUpdated.setText("Last updated:" + " " + element.child(9).text());
            }

            //Signature
            else if (schedule.getType() == 4) {

                holder.description.setText(element.child(8).text());
                holder.time.setText(element.child(3).text());
                holder.locale.setText(element.child(6).text());
                holder.course.setText(element.child(5).text());
                holder.course.setVisibility(View.VISIBLE);
                holder.lastUpdated.setText("Last updated:" + " " + element.child(9).text());
            }
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            Log.e("IndexOutOfBoundEx", "IndexOutOfBoundEx" + e);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}