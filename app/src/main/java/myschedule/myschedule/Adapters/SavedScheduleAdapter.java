package myschedule.myschedule.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.nodes.Element;

import java.util.List;

import myschedule.myschedule.Objects.Schedule;
import myschedule.myschedule.Objects.SchedulePost;
import myschedule.myschedule.R;
import myschedule.myschedule.Activities.ScheduleActivity;


public class SavedScheduleAdapter extends RecyclerView.Adapter<SavedScheduleAdapter.ViewHolder> {

    private List<Schedule> sDataset;
    private Context mcontext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public LinearLayout rowlayout;
        public RelativeLayout relativeLayout;
        public RecyclerView Rview;

        public ImageView scheduleIcon;
        public TextView titel;
        public TextView text;
        public TextView course;


        public ViewHolder(View v) {
            super(v);

            Rview = (RecyclerView) v.findViewById(R.id.saved_schedules_recycler_view);
            cardView = (CardView) v.findViewById(R.id.schedule_card_view);
            rowlayout = (LinearLayout) v.findViewById(R.id.saved_schedules_row_layout);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.saved_schedules_upper_row);
            scheduleIcon = (ImageView) v.findViewById(R.id.saved_schedules_icon);
            titel = (TextView) v.findViewById(R.id.saved_schedules_coursename);
            text = (TextView) v.findViewById(R.id.saved_schedules_next_event);
            course = (TextView) v.findViewById(R.id.saved_schedules_coursecode);
        }
    }

    public SavedScheduleAdapter(List<Schedule> Dataset, Context context) {
        sDataset = Dataset;
        mcontext = context;
    }

    @Override
    public SavedScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_schedules_layout, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // The onClick method loads your saved schedules when you click them
    // and shows them in a new activity with all their containing items.
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Schedule schedule = ((Schedule) mcontext.getApplicationContext());
                    schedule.setPostList(sDataset.get(holder.getAdapterPosition()).getPostList());
                    schedule.setUrl(sDataset.get(holder.getAdapterPosition()).getUrl());
                    schedule.setType(sDataset.get(holder.getAdapterPosition()).getType());
                    schedule.setCode(sDataset.get(holder.getAdapterPosition()).getCode());
                    schedule.setName(sDataset.get(holder.getAdapterPosition()).getName());
                    schedule.setLastUpdated(sDataset.get(holder.getAdapterPosition()).getLastUpdated());

                    Intent intent = new Intent(mcontext, ScheduleActivity.class);
                    mcontext.startActivity(intent);

                }
            });

            holder.cardView.setUseCompatPadding(true);
            holder.titel.setText("");
            holder.course.setText("");
            holder.text.setText("");

            Schedule schedule = sDataset.get(holder.getAdapterPosition());
            SchedulePost nextPost = schedule.getPostList().get(0);
            String nextEventText = mcontext.getResources().getString(R.string.saved_schedules_next_event);

            //Course
            if (schedule.getType() == 1) {
                holder.titel.setText(schedule.getName());
                holder.course.setText(schedule.getCode());
                holder.scheduleIcon.setImageResource(R.drawable.ic_today_black_36dp);
                holder.text.setText((nextEventText + " " + nextPost.getWdAndDate()) + " " + nextPost.getTime());
            }
            //Room
            else if (schedule.getType() == 2) {
                holder.titel.setText(schedule.getName());
                holder.text.setText((nextEventText + " " + nextPost.getWdAndDate()) + " " + nextPost.getTime());
                //holder.course.setText(schedule.getCode());
                holder.scheduleIcon.setImageResource(R.drawable.ic_home_black_36dp);
            }
            //Programme
            else if (schedule.getType() == 3) {
                holder.titel.setText(schedule.getName());
                holder.text.setText((nextEventText +  " " + nextPost.getWdAndDate()) + " " + nextPost.getTime());
                holder.scheduleIcon.setImageResource(R.drawable.ic_date_range_black_36dp);
            }
            //Signature
            else if (schedule.getType() == 4) {
                holder.titel.setText(schedule.getName());
                holder.text.setText(nextEventText +  " " + nextPost.getWdAndDate() + " " + nextPost.getTime());
                //holder.course.setText(nextPost.getCourse());
                holder.scheduleIcon.setImageResource(R.drawable.ic_person_black_36dp);
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", "NullPointerException" + e);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sDataset.size();
    }

}


