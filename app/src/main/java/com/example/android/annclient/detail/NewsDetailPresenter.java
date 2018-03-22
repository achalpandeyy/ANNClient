package com.example.android.annclient.detail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.annclient.data.NewsDetail;
import com.example.android.annclient.data.source.NewsDataRepository;
import com.example.android.annclient.util.schedulers.BaseSchedulerProvider;
import com.example.android.annclient.util.schedulers.SchedulerProvider;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class NewsDetailPresenter implements NewsDetailContract.Presenter {

    @NonNull
    private NewsDataRepository mRepository;
    @NonNull
    private NewsDetailContract.View mView;
    private BaseSchedulerProvider mSchedulerProvider;

    NewsDetailPresenter(@NonNull NewsDataRepository newsDataRepository,
                        @NonNull NewsDetailContract.View view) {
        mRepository = newsDataRepository;
        mView = view;
        mSchedulerProvider = SchedulerProvider.getInstance();
        mView.setPresenter(this);
    }



    void subscribe() {
        requestNewsDetail();
    }

    @Override
    public void unsubscribe() {

    }

    private void requestNewsDetail() {
        //call method on repository here to get news detail
        mRepository.getNewsDetail()
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        new Consumer<NewsDetail>() {
                            @Override
                            public void accept(NewsDetail newsDetail) throws Exception {
                                mView.showNewsDetail(newsDetail);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //on error
                                //show error UI
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                //on complete
                                Log.i("ANNClient", "onComplete requestNewsDetail");
                            }
                        }
                );
    }
}
