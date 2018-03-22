package com.example.android.annclient.util.api;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Utility methods to parse the html document.
 */

class ParseUtils {

    private static final String ANN_URL = "https://www.animenewsnetwork.com/";
    private static final String ID_SEPARATOR = " ";
    private static final String DATE_SEPARATOR = ", ";

    static Elements parseForNewsHeraldBox(Document htmlDoc, int currentPosition) {
        if(htmlDoc != null) {
            return htmlDoc.select(".mainfeed-day " +
                    "[class^=herald box]").eq(currentPosition);
        }
        return null;
    }

    //baseId example - "article192356 column manga"
    //id example - "article192356"
    static String parseForNewsId(Elements newsHeraldBox) {
        String baseId = newsHeraldBox.select("div[data-topics^=article]")
                .attr("data-topics");
        if (baseId != null && !baseId.isEmpty()) {
            return baseId.split(ID_SEPARATOR)[0];
        }
        return null;
    }

    static String parseForHeadline(Elements newsHeraldBox) {
        return newsHeraldBox.select("h3 > a[data-track ^= id]").text();
    }

    static String parseForThumbnailUrl(Elements newsHeraldBox) {
        String relativeUrl = newsHeraldBox.select("div[data-src ^= /]").attr(
                "data-src");
        if(relativeUrl != null && !relativeUrl.isEmpty()) {
            return ANN_URL + relativeUrl;
        }
        return null;
    }

    static String parseForDate(Elements newsHeraldBox) {
        String dateAndTime = newsHeraldBox.select("time[fmt^=%b]").text();
        return dateAndTime.split(DATE_SEPARATOR)[0];
    }

    static String parseForCategory(Elements newsHeraldBox) {
        return newsHeraldBox.select("div.category").text();
    }

    static String parseForPreviewText(Elements newsHeraldBox) {
        return newsHeraldBox.select("span.intro").text();
    }

    static String parseForArticleUrl(Elements newsHeraldBox) {
        String relativeUrl =  newsHeraldBox.select("div.thumbnail > a[href^=/]").attr(
                "href");
        if(!relativeUrl.isEmpty()) {
            return ANN_URL + relativeUrl;
        }
        return relativeUrl;
    }

}
