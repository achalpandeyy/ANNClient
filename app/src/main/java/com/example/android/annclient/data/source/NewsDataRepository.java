package com.example.android.annclient.data.source;

import com.example.android.annclient.data.News;
import com.example.android.annclient.data.NewsDetail;
import com.example.android.annclient.data.source.remote.NewsRemoteDataSource;
import com.example.android.annclient.util.schedulers.SchedulerProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class NewsDataRepository implements NewsDataSource {

    private static final String ANN_URL = "https://www.animenewsnetwork.com/";
    private static final int CONNECTION_TIMEOUT = 5000; //in milliseconds

    private static NewsDataRepository INSTANCE;
    private final NewsDataSource mNewsRemoteDataSource;
    private List<News> mCachedNewsList;
    private boolean mCacheIsDirty = false;
    private boolean mIsDocumentLoading = false;

    private NewsDataRepository(NewsDataSource newsRemoteDataSource) {
        mNewsRemoteDataSource = newsRemoteDataSource;
        mCachedNewsList = new ArrayList<>();
    }

    public static NewsDataRepository getInstance(NewsDataSource newsRemoteDataSource) {
        if(INSTANCE == null) {
            INSTANCE = new NewsDataRepository(newsRemoteDataSource);
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<News>> getNewsList(int currentPage) {
            if(!mCachedNewsList.isEmpty() && mCachedNewsList != null && !mCacheIsDirty) {
                return Observable.just(mCachedNewsList);
            }
            return getAndCacheNewsListFromRemote(currentPage);
    }

    @Override
    public void refreshNewsList() {
        mCacheIsDirty = true;
        if(!mIsDocumentLoading) {
            getHtmlDocument();
        }
    }

    @Override
    public Observable<NewsDetail> getNewsDetail() {
        return mNewsRemoteDataSource.getNewsDetail();
    }


    private Observable<List<News>> getAndCacheNewsListFromRemote(int currentPage) {
        return mNewsRemoteDataSource.getNewsList(currentPage)
                .doOnNext(new Consumer<List<News>>() {
                    @Override
                    public void accept(List<News> newsList) throws Exception {
                        if(mCachedNewsList == null) {
                            mCachedNewsList = new ArrayList<>();
                        }
                        mCachedNewsList.addAll(newsList);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mCacheIsDirty = true;
                    }
                });
    }
    
    private void getHtmlDocument() {
        mIsDocumentLoading = true;
        Observable.fromCallable(
                new Callable<Document>() {

                    @Override
                    public Document call() throws Exception {
                        return Jsoup.connect(ANN_URL).timeout(CONNECTION_TIMEOUT).get();
                    }

                }
        )
                .subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(
                        new Consumer<Document>() {
                            @Override
                            public void accept(Document document) throws Exception {
                                //the document is successfully fetched
                                NewsRemoteDataSource.setListHtmlDoc(document);
                                mIsDocumentLoading = false;

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //error while fetching the document
                                mIsDocumentLoading = false;
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                //when the operation completes
                                mIsDocumentLoading = false;
                            }
                        });
    }
}
