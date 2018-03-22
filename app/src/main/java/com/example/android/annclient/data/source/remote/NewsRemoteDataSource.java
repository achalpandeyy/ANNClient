package com.example.android.annclient.data.source.remote;

import android.content.Context;

import com.example.android.annclient.data.News;
import com.example.android.annclient.data.NewsDetail;
import com.example.android.annclient.data.source.NewsDataSource;
import com.example.android.annclient.util.api.ANNApi;

import org.jsoup.nodes.Document;

import java.util.List;

import io.reactivex.Observable;

public class NewsRemoteDataSource implements NewsDataSource {

    private static NewsRemoteDataSource INSTANCE;
    private Context mContext;
    private static Document mListHtmlDoc;
    private static Document mDetailHtmlDoc;

    private NewsRemoteDataSource(Context context) {
        mContext = context;
    }

    public static void setListHtmlDoc(Document listHtmlDoc) {
        NewsRemoteDataSource.mListHtmlDoc = listHtmlDoc;
    }

    public static void setDetailHtmlDoc(Document detailHtmlDoc) {
        mDetailHtmlDoc = detailHtmlDoc;
    }

    public static NewsRemoteDataSource getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new NewsRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<News>> getNewsList(int currentPage) {
        //set the list html document
        ANNApi.getInstance(mContext).setListHtmlDoc(mListHtmlDoc);
        return ANNApi.getInstance(mContext).getNewsList(currentPage);
    }

    @Override
    public void refreshNewsList() {
        //do nothing
    }

    @Override
    public Observable<NewsDetail> getNewsDetail() {
        //set the detail html document
        if(mDetailHtmlDoc != null) {
            ANNApi.getInstance(mContext).setDetailHtmlDoc(mDetailHtmlDoc);
            return ANNApi.getInstance(mContext).getNewsDetail();
        }
        return Observable.empty();
    }

}
