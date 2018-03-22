package com.example.android.annclient.data;

/**
 * POJO News.
 */

public class News {

    private String newsId;

    private String headline;

    private String thumbnailUrl;

    private String date;

    private String category;

    private String previewText;

    private String articleUrl;

    public News(){}

    public News(String newsId, String headline, String thumbnailUrl, String date, String category, String previewText, String articleUrl) {
        this.newsId = newsId;
        this.headline = headline;
        this.thumbnailUrl = thumbnailUrl;
        this.date = date;
        this.category = category;
        this.previewText = previewText;
        this.articleUrl = articleUrl;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPreviewText() {
        return previewText;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
