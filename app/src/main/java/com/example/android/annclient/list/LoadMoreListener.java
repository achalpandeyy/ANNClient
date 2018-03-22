package com.example.android.annclient.list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

abstract class LoadMoreListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 3; //arbitrary, may change at a later date
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    private RecyclerView.LayoutManager layoutManager;

    LoadMoreListener(LinearLayoutManager linearLayoutManager, int currentPage) {
        layoutManager = linearLayoutManager;
        this.currentPage = currentPage;
    }

    int getCurrentPage() {
        return currentPage;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();

        //if current total item count is less than previous total item count
        //assuming list is invalidated, since with each load data must increase
        //so resetting the list to it's initial state.
        if(previousTotalItemCount > totalItemCount) {
            currentPage = startingPageIndex;
            previousTotalItemCount = totalItemCount;
            if(totalItemCount == 0) {
                loading = true;
            }
        }

        //if the data is still loading, we check to see if the data count has changed.
        if(loading && totalItemCount > previousTotalItemCount) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        //load more data when we have breached the threshold
        if(!loading && ((lastVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount)) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }

    }

    abstract void onLoadMore(int currentPage);

    void resetState() {
        currentPage = startingPageIndex;
        previousTotalItemCount = 0;
        loading = true;
    }

}
