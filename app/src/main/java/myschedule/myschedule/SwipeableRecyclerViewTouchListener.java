package myschedule.myschedule;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener.SwipeListener;


/**
 * Created by Kevin on 4/7/2016.
 */
public class SwipeableRecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    // Fixed properties
    private RecyclerView mRecyclerView;
    private static SwipeListener mSwipeListener;
    private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero


    public SwipeableRecyclerViewTouchListener(RecyclerView recyclerView, SwipeListener listener){

            mRecyclerView = recyclerView;
            mSwipeListener = listener;

    }



    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
