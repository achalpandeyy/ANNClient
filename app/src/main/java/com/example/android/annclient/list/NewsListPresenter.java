package com.example.android.annclient.list;

import android.support.annotation.NonNull;

import com.example.android.annclient.data.News;
import com.example.android.annclient.data.source.NewsDataRepository;
import com.example.android.annclient.util.schedulers.BaseSchedulerProvider;
import com.example.android.annclient.util.schedulers.SchedulerProvider;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class NewsListPresenter implements NewsListContract.Presenter {

    @NonNull
    private NewsDataRepository mRepository;

    @NonNull
    private NewsListContract.View mNewsListView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mDisposables;

    private boolean mFirstLoad = true;

    NewsListPresenter(@NonNull NewsDataRepository repository, @NonNull NewsListContract.View view) {
        mRepository = repository;
        mNewsListView = view;
        mSchedulerProvider = SchedulerProvider.getInstance();
        mDisposables = new CompositeDisposable();
        mNewsListView.setPresenter(this);
    }

    private void loadNewsList(int currentPage, boolean forceUpdate, boolean showLoadingUI,
                              final boolean append) {

        mNewsListView.setLoadingIndicator(showLoadingUI);

        if(forceUpdate) {
            mRepository.refreshNewsList();
        }

            Disposable disposable = mRepository.getNewsList(currentPage)
                    .subscribeOn(mSchedulerProvider.computation())
                    .observeOn(mSchedulerProvider.ui())
                    .subscribe(
                            new Consumer<List<News>>() {
                                @Override
                                public void accept(List<News> newsList) throws Exception {
                                    if(append) {
                                        mNewsListView.appendNewsList(newsList);
                                    }
                                    else {
                                        mNewsListView.showNewsList(newsList);
                                    }
                                    mNewsListView.hideRetry();
                                }
                            },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    mNewsListView.showRetry();
                                    mNewsListView.hideNewsList();
                                    mNewsListView.setLoadingIndicator(false);
                                }
                            },
                            new Action() {
                                @Override
                                public void run() throws Exception {
                                    mNewsListView.setLoadingIndicator(false);
                                }
                            });
            mDisposables.add(disposable);

    }

    void subscribe(int currentPage) {
        loadNewsList(currentPage, false, false);
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    @Override
    public void loadNewsList(int currentPage, boolean forceUpdate, boolean append) {
        loadNewsList(currentPage, forceUpdate || mFirstLoad, true, append);
        mFirstLoad = false;
    }

}
