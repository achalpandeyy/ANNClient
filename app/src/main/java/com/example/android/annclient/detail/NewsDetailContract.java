package com.example.android.annclient.detail;

import com.example.android.annclient.base.BasePresenter;
import com.example.android.annclient.base.BaseView;
import com.example.android.annclient.data.NewsDetail;

public interface NewsDetailContract {

    interface View extends BaseView<Presenter> {

        void showNewsDetail(NewsDetail newsDetail);

        void showError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }

}
