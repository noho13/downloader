package com.normanhoeller.downloader.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by norman on 19/09/14.
 * ImageView that knows about an url and wether a download is running or not
 */
public class DownloadImageView extends ImageView {

    private boolean isDownloadActive;
    private String url;
    private int positionInAdapter;
    private boolean isInCache;
    private boolean hasFailed;



    public DownloadImageView(Context context) {
        super(context);
    }

    public DownloadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DownloadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isDownloadActive() {
        return isDownloadActive;
    }

    public void setDownloadActive(boolean isDownloadActive) {
        this.isDownloadActive = isDownloadActive;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPositionInAdapter() {
        return positionInAdapter;
    }

    public void setPositionInAdapter(int positionInAdapter) {
        this.positionInAdapter = positionInAdapter;
    }

    public boolean isInCache() {
        return isInCache;
    }

    public void setInCache(boolean isInCache) {
        this.isInCache = isInCache;
    }

    public boolean isHasFailed() {
        return hasFailed;
    }

    public void setHasFailed(boolean hasFailed) {
        this.hasFailed = hasFailed;
    }
}
