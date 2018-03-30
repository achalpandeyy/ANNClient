package com.example.android.annclient.util.api;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Helper class to modify the html data.
 */

class HtmlHelper {

    static String getHtmlContent(Document detailHtmlDoc) {
        Elements contentElements = detailHtmlDoc.select(".KonaBody");
        String htmlData = fixImageUrls(contentElements);
        htmlData = assembleHtml(htmlData);
        return htmlData;
    }

    private static String assembleHtml(String htmlData) {
        final String openingHtml = "<!doctype html>\n" + "<html>\n" + "  <head>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Montserrat:400,500\" rel=\"stylesheet\">\n" +
                "  </head>\n" + "  <body>\n" + " \n" + "</body>\n" + "</html>\n";
        final String closingHtml = "</body>\n" + "</html>";

        return openingHtml + htmlData + closingHtml;
    }

    private static String fixImageUrls(Elements contentElements) {
        final String BASE_URL = "https://www.animenewsnetwork.com/";
        Elements imageElements = contentElements.select("img");
        int i = 0;
        Elements e = imageElements.eq(i);
        while(!e.isEmpty()) {
            String absUrl = e.attr("data-src");
            if(absUrl.isEmpty()) {
                absUrl = e.attr("abs:src");
            }
            else {
                absUrl = BASE_URL + absUrl;
            }
            e.attr("src", absUrl);
            i++;
            e = imageElements.eq(i);
        }
        return contentElements.html();
    }
}
