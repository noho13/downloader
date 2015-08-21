package com.normanhoeller.downloader.background;

import com.normanhoeller.downloader.utils.DownloadImageView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by norman on 19/09/14.
 * manages a runnable and its results
 */
public class DownloadTask {

    private String url;
    private File mSaveDestination;
    public static final int BUFFER_SIZE = 4096;
    private File mResult;
    private WeakReference<DownloadImageView> imageViewWeakReference;

    private DownloadRunnable mDownloadRunnable;
    private Thread mCurrentThread;
    private DownloadManager mDownloadManager;

    public DownloadTask() {
        mDownloadRunnable = new DownloadRunnable(this);
        mDownloadManager = DownloadManager.getInstance();
    }

    public void setCurrentThread(Thread currentThread) {
        mCurrentThread = currentThread;
    }

    public void handleState(int state) {
        mDownloadManager.handleState(this, state);
    }

    public Thread getCurrentThread() {
        return mCurrentThread;
    }

    public DownloadRunnable getDownloadRunnable() {
        return mDownloadRunnable;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlString() {
        return url;
    }

    public File getSaveDestination() {
        return mSaveDestination;
    }

    public void setSaveDestination(File saveDestination) {
        this.mSaveDestination = saveDestination;
    }

    public File getResult() {
        return mResult;
    }

    public void setResult(File result) {
        this.mResult = result;
    }

    public WeakReference<DownloadImageView> getImageViewWeakReference() {
        return imageViewWeakReference;
    }

    public void setImageViewWeakReference(DownloadImageView imageView) {
        imageViewWeakReference = new WeakReference<DownloadImageView>(imageView);
    }
}
