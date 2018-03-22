package com.example.android.annclient.list;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * View for swipe up to refresh feature. Will be configured in the hosted fragment.
 */

public class NewsListSwipeRefreshLayout extends SwipeRefreshLayout {

    private View mChildView;

    public NewsListSwipeRefreshLayout(Context context) {
        super(context);
    }

    public NewsListSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if(mChildView != null) {
            //direction < 0 represents upward scroll
            return mChildView.canScrollVertically(-1);
        }
        return super.canChildScrollUp();
    }

    public void setChildView(View childView) {
        mChildView = childView;
    }
}
