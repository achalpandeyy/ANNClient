package com.example.android.annclient.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.annclient.R;
import com.example.android.annclient.base.BaseActivity;
import com.example.android.annclient.data.source.NewsDataRepository;
import com.example.android.annclient.data.source.remote.NewsRemoteDataSource;
import com.example.android.annclient.list.NewsListFragment;
import com.example.android.annclient.util.schedulers.SchedulerProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class NewsDetailActivity extends BaseActivity {

    private static final String ARG_ARTICLE_URL = "to save article url between configuration changes";
    private static final String ARG_LOADING = "to save loading state between configuration changes";

    private String mArticleUrl;
    private boolean mLoading;
    private NewsDataRepository mRepository;
    private NewsDetailPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        if(savedInstanceState == null) {
            //activity is not recreated
            mArticleUrl = getIntent().getStringExtra(NewsListFragment.EXTRA_ARTICLE_URL);
            mLoading = false;
        }
        else {
            //activity is recreated
            mArticleUrl = savedInstanceState.getString(ARG_ARTICLE_URL);
            mLoading = savedInstanceState.getBoolean(ARG_LOADING);
        }

        //if not already loading, load the detail html doc
        if(!mLoading) {
            getAndSetDetailHtmlDoc(mArticleUrl);
        }

        NewsDetailFragment fragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.container_news_detail);
        //Log.i("debug", "fragment: " + fragment);
        if(fragment != null) {
            addFragment(getSupportFragmentManager(), R.id.container_news_detail, fragment);
            mRepository = NewsDataRepository.getInstance(NewsRemoteDataSource.getInstance(this));
            mPresenter = new NewsDetailPresenter(mRepository, fragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ARTICLE_URL, mArticleUrl);
        outState.putBoolean(ARG_LOADING, mLoading);
    }

    private void getAndSetDetailHtmlDoc(final String articleUrl) {
        if(articleUrl != null && !articleUrl.isEmpty()) {
            mLoading = true;
            Observable.fromCallable(
                    new Callable<Document>() {

                        @Override
                        public Document call() throws Exception {
                            return Jsoup.connect(articleUrl).get();  //set timeout later
                        }
                    }
            )
                    .subscribeOn(SchedulerProvider.getInstance().io())
                    .observeOn(SchedulerProvider.getInstance().ui())
                    .subscribe(
                            new Consumer<Document>() {
                                @Override
                                public void accept(Document detailHtmlDoc) throws Exception {
                                    mLoading = false;
                                    //Toast.makeText(NewsDetailActivity.this, "Got the doc!", Toast.LENGTH_LONG).show();
                                    NewsRemoteDataSource.setDetailHtmlDoc(detailHtmlDoc);
                                    //add the fragment only when the doc is successfully fetched
                                    Log.d("debug", "document successfully fetched.");
                                    createAndAddNewsDetailFragment();

                                }
                            },

                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    mLoading = false;
                                    //on error
                                    //show UI
                                }
                            },

                            new Action() {
                                @Override
                                public void run() throws Exception {
                                    mLoading = false;
                                    //on complete
                                }
                            }
                    );
        }

    }

    private void createAndAddNewsDetailFragment() {
        NewsDetailFragment fragment = NewsDetailFragment.newInstance();
        addFragment(getSupportFragmentManager(), R.id.container_news_detail, fragment);
        mRepository = NewsDataRepository.getInstance(NewsRemoteDataSource.getInstance(this));
        mPresenter = new NewsDetailPresenter(mRepository, fragment);
    }
}
