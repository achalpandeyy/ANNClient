package com.example.android.annclient.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.annclient.R;
import com.example.android.annclient.data.News;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<News> mNewsList;
    private OnNewsClickListener mOnNewsClickListener;

    NewsListAdapter() {
        mNewsList = new ArrayList<>();
    }

    void setOnNewsClickListener(OnNewsClickListener onNewsClickListener) {
        mOnNewsClickListener = onNewsClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NewsViewHolder) holder).bind(mNewsList.get(position));
        final News news = mNewsList.get(position);
        //if the onNewsClickListener is not null, set up on Click listener on the view
        if(mOnNewsClickListener != null) {
            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnNewsClickListener.onNewsClicked(news);
                        }
                    }
            );
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList != null ? mNewsList.size() : 0;
    }

    void setNewsList(List<News> newsList) {
        if(newsList == null) {
            throw new IllegalArgumentException("The news list must not be null");
        }
        mNewsList = newsList;
        notifyDataSetChanged();
    }

    void addNewsList(List<News> newsList) {
        if(newsList == null || newsList.size() == 0) {
            return;
        }
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }

    interface OnNewsClickListener {

        void onNewsClicked(News news);

    }

}
