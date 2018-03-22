package com.example.android.annclient.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.annclient.R;
import com.example.android.annclient.data.News;

import butterknife.BindView;
import butterknife.ButterKnife;

class NewsViewHolder extends RecyclerView.ViewHolder {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.iv_thumbnail)
    ImageView mThumbnailImageView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_headline)
    TextView mHeadlineTextView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_date)
    TextView mDateTextView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_preview)
    TextView mPreviewTextView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_category)
    TextView mCategoryTextView;

    NewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bind(News news) {
        GlideApp.with(this.mThumbnailImageView.getContext())
                .load(news.getThumbnailUrl()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mThumbnailImageView); //maybe add a place holder and an error image in the future
        mHeadlineTextView.setText(news.getHeadline());
        mDateTextView.setText(news.getDate());
        mPreviewTextView.setText(news.getPreviewText());
        mCategoryTextView.setText(news.getCategory());
    }
}
