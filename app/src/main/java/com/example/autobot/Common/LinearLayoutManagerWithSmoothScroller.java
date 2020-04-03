package com.example.autobot.Common;

import android.content.Context;
import android.graphics.PointF;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
/**
 * this is a class whichc extends LinearLayoutManager, it is to used for Recycler View.
 */
public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager {
    public LinearLayoutManagerWithSmoothScroller(Context context) {
        super(context);
    }

    public LinearLayoutManagerWithSmoothScroller(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * @param recyclerView the RecyclerView
     * @param state the RecyclerView.State
     * @param position the position the item at
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {

        public TopSnappedSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }


    /**
     * @param targetPosition the position the item at
     */
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        return LinearLayoutManagerWithSmoothScroller.this.computeScrollVectorForPosition(targetPosition);
    }
}
