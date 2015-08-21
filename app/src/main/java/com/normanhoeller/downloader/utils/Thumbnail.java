package com.normanhoeller.downloader.utils;

/**
 * Created by norman on 20/09/14.
 * POJO used in the adapter - holds a resourceId for a Drawable as well as a url string for the download
 */
public class Thumbnail {

    private int mDrawableResourceId;
    private String mUrl;

    public Thumbnail(int drawableResourceId, String url) {
        mDrawableResourceId = drawableResourceId;
        mUrl = url;
    }

    public int getDrawableResourceId() {
        return mDrawableResourceId;
    }

    public void setDrawableResourceId(int drawableResourceId) {
        this.mDrawableResourceId = drawableResourceId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }
}
