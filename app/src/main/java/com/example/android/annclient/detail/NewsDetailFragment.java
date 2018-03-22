package com.example.android.annclient.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.android.annclient.R;
import com.example.android.annclient.base.BaseFragment;
import com.example.android.annclient.data.NewsDetail;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsDetailFragment extends BaseFragment implements NewsDetailContract.View {

    @BindView(R.id.news_detail_wv_content)
    WebView mNewsWebView;

    private NewsDetailContract.Presenter mPresenter;

    public static NewsDetailFragment newInstance() {
        return new NewsDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        ButterKnife.bind(this, view);

        //configure web view here
        //setting up font size of web view here
        int fontSize = (int) getResources().getDimension(R.dimen.webview_font_size);
        mNewsWebView.getSettings().setDefaultFontSize(fontSize);
        return view;
    }

    @Override
    public void setPresenter(@NonNull NewsDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NewsDetailPresenter) mPresenter).subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void showNewsDetail(NewsDetail newsDetail) {
        if(!isActive() || getView() == null) {
            return;
        }
        if(newsDetail == null) {
            return;
        }
        String htmlData = newsDetail.getHtmlContent();
        Log.d("debug", htmlData);
        mNewsWebView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html; charset=UTF-8", null, null);

    }

    @Override
    public void showError() {
        //show error message in a snack bar
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

}
