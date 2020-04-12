package com.juha.blogmap;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SearchBlog extends AsyncTask<Void, Void, Integer> {

    String TAG = "SearchBlogT";
    private String query;
    public SearchBlog(String placeName) {
        this.query = placeName;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int result = 0;
        try {
            String url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=" + query;

            Document doc = Jsoup.connect(url).get();
            Elements contents = doc.select("div.blog.section._blogBase._prs_blg > div > span");
            String[] texts = contents.text().split(" / ");

            String num = texts[1].replace("건", "");
            result = Integer.parseInt(num.replace(",", ""));
        } catch (IOException e) {
            Log.i(TAG, "Error");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        Log.i(TAG, String.valueOf(integer));
        Log.i(TAG, "POST EXECUTE");
    }
}
