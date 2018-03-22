package com.example.android.annclient.splash;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.annclient.R;
import com.example.android.annclient.data.source.remote.NewsRemoteDataSource;
import com.example.android.annclient.list.NewsListActivity;
import com.example.android.annclient.util.schedulers.SchedulerProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class SplashActivity extends Activity {

    private static final String ANN_URL = "https://www.animenewsnetwork.com/";
    private static final int CONNECTION_TIMEOUT = 5000; //in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        getHtmlDocument();
    }

    private void getHtmlDocument() {
        Observable.fromCallable(
                new Callable<Document>() {

                    @Override
                    public Document call() throws Exception {
                        return Jsoup.connect(ANN_URL).get(); //add timeout
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
                                navigate();
                                finish();

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //error while fetching the document
                                //show error UI
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                //when the operation completes
                            }
                        });
    }

    private void navigate() {
        Intent intent = new Intent(this, NewsListActivity.class);
        startActivity(intent);
    }

}
