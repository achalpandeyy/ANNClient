package com.example.android.annclient.util.api;

import android.content.Context;

import com.example.android.annclient.data.News;
import com.example.android.annclient.data.NewsDetail;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Parses news items from the html document.
 * Page size = 5 news items.
 */

public class ANNApi {

    private static final int PAGE_SIZE = 5;

    private static ANNApi INSTANCE;
    private Context mContext;
    private Document mListHtmlDoc;
    private Document mDetailHtmlDoc;

    private ANNApi(Context context) {
        mContext = context;
    }

    public void setListHtmlDoc(Document listHtmlDoc) {
        mListHtmlDoc = listHtmlDoc;
    }

    public void setDetailHtmlDoc(Document detailHtmlDoc) {
        mDetailHtmlDoc = detailHtmlDoc;
    }

    public static ANNApi getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new ANNApi(context);
        }
        return INSTANCE;
    }

    public Observable<List<News>> getNewsList(int currentPage) {
        List<News> newsList = new ArrayList<>();
        for(int i = (PAGE_SIZE * currentPage); i < (PAGE_SIZE * (currentPage + 1)); i++) {
            News news = getNews(i);
            if(news.getNewsId() == null || news.getNewsId().isEmpty()) {
                break;
            }
            newsList.add(news);
        }
        return Observable.just(newsList);
    }

    public Observable<NewsDetail> getNewsDetail() {
       NewsDetail newsDetail = new NewsDetail();
       newsDetail.setHtmlContent(HtmlHelper.getHtmlContent(mDetailHtmlDoc));
       return Observable.just(newsDetail);
    }

    private News getNews(int currentPosition) {
        News news = new News();
        Elements newsHeraldBox = ParseUtils.parseForNewsHeraldBox(mListHtmlDoc, currentPosition);
        news.setNewsId(ParseUtils.parseForNewsId(newsHeraldBox));
        news.setHeadline(ParseUtils.parseForHeadline(newsHeraldBox));
        news.setThumbnailUrl(ParseUtils.parseForThumbnailUrl(newsHeraldBox));
        news.setDate(ParseUtils.parseForDate(newsHeraldBox));
        news.setCategory(ParseUtils.parseForCategory(newsHeraldBox));
        news.setPreviewText(ParseUtils.parseForPreviewText(newsHeraldBox));
        news.setArticleUrl(ParseUtils.parseForArticleUrl(newsHeraldBox));
        return news;
    }

}
