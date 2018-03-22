package com.example.android.annclient.list;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.android.annclient.R;
import com.example.android.annclient.base.BaseActivity;
import com.example.android.annclient.data.source.NewsDataRepository;
import com.example.android.annclient.data.source.remote.NewsRemoteDataSource;



public class NewsListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);


        NewsListFragment newsListFragment = (NewsListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_news_list);
        if(newsListFragment == null) {
            newsListFragment = NewsListFragment.newInstance();
            addFragment(getSupportFragmentManager(), R.id.container_news_list, newsListFragment);
        }

        NewsDataRepository repository = NewsDataRepository.getInstance(
                NewsRemoteDataSource.getInstance(this)
        );

        NewsListPresenter presenter = new NewsListPresenter(repository, newsListFragment);
    }


}
