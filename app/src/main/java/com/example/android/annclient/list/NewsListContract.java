package com.example.android.annclient.list;

import com.example.android.annclient.base.BasePresenter;
import com.example.android.annclient.base.BaseView;
import com.example.android.annclient.data.News;

import java.util.List;

/**
 * Created by achal on 11/3/18.
 */

public interface NewsListContract {

    interface View extends BaseView<Presenter> {

        void showNewsList(List<News> newsList);

        void appendNewsList(List<News> newsList);

        void hideNewsList();

        void setLoadingIndicator(boolean active);

        void showRetry();

        void hideRetry();

        void showError(String message);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadNewsList(int currentPosition, boolean forceUpdate, boolean append);

    }
}
