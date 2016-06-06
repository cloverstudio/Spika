package com.clover_studio.spikachatmodule.utils;

import android.os.AsyncTask;
import android.webkit.URLUtil;

import com.clover_studio.spikachatmodule.models.ParsedUrlData;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;

/**
 * Created by ubuntu_ivo on 21.03.16..
 */
public class ParseUrlLinkMetadata extends AsyncTask<Void, Void, Void>{

    private String url;
    private OnUrlParsed listener;
    private ParsedUrlData resultData;
    private JSONObject resultJson;

    public ParseUrlLinkMetadata(String url, OnUrlParsed listener){
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(listener != null){
            if(resultData == null || resultJson == null){
                listener.onUrlParsed(null);
            }else{
                listener.onUrlParsed(resultData);
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        resultData = new ParsedUrlData();

        try {

            if(!url.startsWith("http")){
                url = "http://" + url;
            }

            URL inputUrl = new URL(url.trim());
            Document document = Jsoup.connect(url).maxBodySize(0).userAgent("Desktop").get();
            Element element;

            if ((element = document.head().select("meta[property=og:title]").first()) != null) {
                resultData.title = element.attr("content");
            } else {
                resultData.title = document.title();
            }

            if ((element = document.head().select("meta[property=og:site_name]").first()) != null) {
                resultData.siteName = element.attr("content");
            } else {
                resultData.siteName = document.title();
            }

            if ((element = document.head().select("meta[property=og:description]").first()) != null) {
                resultData.desc = element.attr("content");
            } else if ((element = document.head().select("meta[name=description]").first()) != null) {
                resultData.desc = element.attr("content");
            }

            if ((element = document.head().select("meta[property=og:image]").first()) != null) {
                resultData.imageUrl = element.attr("content");
            }else if ((element = document.head().select("link[rel=apple-touch-icon]").first()) != null) {
                resultData.imageUrl = element.attr("href");
            } else if ((element = document.head().select("link[rel=apple-touch-icon-precomposed]").first()) != null) {
                resultData.imageUrl = element.attr("href");
            } else if ((element = document.head().select("meta[property=og:image]").first()) != null) {
                resultData.imageUrl = element.attr("content");
            }

            if (resultData.imageUrl != null && !URLUtil.isValidUrl(resultData.imageUrl)) {

                if (resultData.imageUrl.startsWith("//")) {
                    resultData.imageUrl = inputUrl.getProtocol() + ":" + resultData.imageUrl;
                } else {
                    resultData.imageUrl = inputUrl.getProtocol() + "://" + inputUrl.getHost() + "/" + resultData.imageUrl;
                }

                if (!URLUtil.isValidUrl(resultData.imageUrl)) {
                    resultData.imageUrl = null;
                }
            }

            resultData.url = url.trim();
            resultData.host = inputUrl.getHost();

            resultJson = new JSONObject();
            resultJson.put("url", resultData.url);
            resultJson.put("host", resultData.host.trim());

            if (resultData.imageUrl != null) {
                resultJson.put("imageUrl", resultData.imageUrl.trim());
            }
            if (resultData.title != null) {
                resultJson.put("title", resultData.title.trim());
            }
            if (resultData.desc != null) {
                resultJson.put("desc", resultData.desc.trim());
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public interface OnUrlParsed {
        void onUrlParsed(ParsedUrlData data);
    }

}
