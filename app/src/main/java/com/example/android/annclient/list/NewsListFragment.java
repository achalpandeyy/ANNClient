package com.example.android.annclient.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.annclient.R;
import com.example.android.annclient.base.BaseFragment;
import com.example.android.annclient.data.News;
import com.example.android.annclient.detail.NewsDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsListFragment extends BaseFragment implements NewsListContract.View, NewsListAdapter.OnNewsClickListener {

    private static final String ARG_CURRENT_PAGE = "mCurrentPage";
    public static final String EXTRA_ARTICLE_URL = "mArticleUrl";

    @BindView(R.id.swipe_refresh_view)
    NewsListSwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.rv_news_list)
    RecyclerView mNewsRecyclerView;

    @BindView(R.id.tv_loading_error)
    TextView mLoadingErrorView;

    private NewsListContract.Presenter mPresenter;
    private LoadMoreListener mLoadMoreListener;
    private NewsListAdapter.OnNewsClickListener mNewsClickListener;
    private NewsListAdapter mNewsAdapter;
    private int mCurrentPage;


    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsAdapter = new NewsListAdapter();
        //set up onItemClickListener on it to open detail view of the news
        mNewsAdapter.setOnNewsClickListener(this);
        if(savedInstanceState != null) {
            mCurrentPage = savedInstanceState.getInt(ARG_CURRENT_PAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mNewsRecyclerView.setAdapter(mNewsAdapter);
        mNewsRecyclerView.setLayoutManager(linearLayoutManager);

        mLoadMoreListener = new LoadMoreListener(linearLayoutManager, mCurrentPage) {
            @Override
            void onLoadMore(int currentPage) {
                Log.i("News", "onLoadMore called");
                mPresenter.loadNewsList(currentPage, false, true);
            }
        };

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        mSwipeRefreshLayout.setChildView(mNewsRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mLoadMoreListener.resetState();
                        mPresenter.loadNewsList(mLoadMoreListener.getCurrentPage(), false, false);
                    }
                }
        );

        return rootView;
    }

    @Override
    public void setPresenter(@NonNull NewsListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NewsListPresenter)mPresenter).subscribe(mLoadMoreListener.getCurrentPage());
        mNewsRecyclerView.addOnScrollListener(mLoadMoreListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
        mNewsRecyclerView.removeOnScrollListener(mLoadMoreListener);
    }

    @Override
    public void showNewsList(List<News> newsList) {
        mNewsRecyclerView.setVisibility(View.VISIBLE);
            mNewsAdapter.setNewsList(newsList);

    }

    @Override
    public void appendNewsList(List<News> newsList) {
        mNewsRecyclerView.setVisibility(View.VISIBLE);
            mNewsAdapter.addNewsList(newsList);

    }

    @Override
    public void hideNewsList() {
        mNewsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if(!isActive() || getView() == null) {
            return;
        }

        //post method allows you to the Runnable object in the message queue
        //this makes sure that the code inside Runnable is executed after UI is done setting up
        mSwipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(active);
                    }
                }
        );
    }

    @Override
    public void onNewsClicked(News news) {
        if(news != null && !news.getNewsId().isEmpty()) {
            String articleUrl = news.getArticleUrl();
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(EXTRA_ARTICLE_URL, articleUrl);
            startActivity(intent);
        }
    }

    @Override
    public void showRetry() {
        //make the view visible which prompts the user "Tap to refresh"
    }

    @Override
    public void hideRetry() {
        //make the view invisible which prompts the user "Tap to refresh"
    }

    @Override
    public void showError(String message) {
        //make the connection lost graphic visible
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    //implementing tap to refresh
    @OnClick(R.id.tv_loading_error)
    void reload() {
        mLoadMoreListener.resetState();
        mPresenter.loadNewsList(mLoadMoreListener.getCurrentPage(), true, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_CURRENT_PAGE, mLoadMoreListener.getCurrentPage());
    }
}
