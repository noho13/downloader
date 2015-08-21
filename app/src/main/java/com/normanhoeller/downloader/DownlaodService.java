package com.normanhoeller.downloader;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.normanhoeller.downloader.background.DownloadManager;
import com.normanhoeller.downloader.background.DownloadTask;
import com.normanhoeller.downloader.utils.DownloadImageView;

import java.util.HashMap;

/**
 * Created by norman on 15/09/14.
 */
public class DownlaodService extends Service {

    private IBinder mBinder = new LocalService();
    private DownloadManager mDownloadManager = DownloadManager.getInstance();
    private HashMap<String, DownloadTask> mDownloadTasks = new HashMap<String, DownloadTask>();

    public class LocalService extends Binder {
        public DownlaodService getService() {
            return DownlaodService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String getText() {
        return "I am bound";
    }

    public void downlaodBitmap(String url, DownloadImageView downloadImageView) {
        DownloadTask task = new DownloadTask();
        task.setUrl(url);
        task.setSaveDestination(getExternalCacheDir());
        task.setImageViewWeakReference(downloadImageView);
        mDownloadTasks.put(url, task);
        mDownloadManager.startDownload(task);
    }

    public void stopDownload(String url) {
        DownloadTask task = mDownloadTasks.get(url);
        mDownloadTasks.remove(url);
        if (task != null) {
            mDownloadManager.stopDownload(task);
        }
    }

}
