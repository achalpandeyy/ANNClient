package com.example.android.annclient.data.source;

import com.example.android.annclient.data.News;
import com.example.android.annclient.data.NewsDetail;

import java.util.List;

import io.reactivex.Observable;

public interface NewsDataSource {

    Observable<List<News>> getNewsList(int currentPosition);

    void refreshNewsList();

    Observable<NewsDetail> getNewsDetail();

}
