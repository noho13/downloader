package com.normanhoeller.downloader.background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.normanhoeller.downloader.utils.DownloadImageView;

import java.lang.ref.WeakReference;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by norman on 19/09/14.
 * class responsible for managing a {@link java.util.concurrent.ThreadPoolExecutor}
 */
public class DownloadManager {

    public static final int DOWNLOAD_FAILED = 234;
    public static final int DOWNLOAD_COMPLETED = 213;
    public static final int DOWNLOAD_STARTED = 978;
    public static final int DOWNLOAD_INTERRUPTED = 635;

    private static final String TAG = DownloadManager.class.getSimpleName();
    private final static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final static int KEEP_THREAD_IDLE_TIME = 1;
    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private static DownloadManager mDownloadManagerInstance;

    private ThreadPoolExecutor mThreadPoolExecutor;

    private BlockingQueue<Runnable> mDownloadRunnableQueue;


    private Handler mHandler;

    public static DownloadManager getInstance() {

        if (mDownloadManagerInstance == null) {
            mDownloadManagerInstance = new DownloadManager();
        }

        return mDownloadManagerInstance;
    }

    private DownloadManager() {

        mDownloadRunnableQueue = new LinkedBlockingQueue<Runnable>();
        mThreadPoolExecutor = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_THREAD_IDLE_TIME, TIME_UNIT, mDownloadRunnableQueue);

        mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                DownloadTask task = (DownloadTask) msg.obj;

                int downloadState = msg.what;

                switch (downloadState) {
                    case DOWNLOAD_STARTED:
                        Log.d(TAG, "download started: " + task.toString());
                        setBackgroundColor(task.getImageViewWeakReference(), Color.GREEN, false);
                        break;
                    case DOWNLOAD_COMPLETED:
                        Log.d(TAG, "download complete: " + task.toString());
                        Log.d(TAG, "file path " + task.getResult());
//                        setBackgroundColor(task.getImageViewWeakReference(), Color.WHITE);
                        setDownloadedDrawable(task);
                        break;
                    case DOWNLOAD_FAILED:
                        Log.d(TAG, "download failed: " + task.toString());
                        setBackgroundColor(task.getImageViewWeakReference(), Color.RED, true);
                        break;
                    case DOWNLOAD_INTERRUPTED:
                        Log.d(TAG, "download interrupted: " + task.toString());
                        setBackgroundColor(task.getImageViewWeakReference(), Color.BLUE, false);
                        break;
                    default:
                        Log.d(TAG, "action not defined: " + task.toString());
                }
            }
        };
    }

    private void setBackgroundColor(WeakReference<DownloadImageView> imageViewWeakReference, int color, boolean failed) {
        if (imageViewWeakReference != null) {
            DownloadImageView view = imageViewWeakReference.get();
            if (view != null) {
                view.setImageDrawable(new ColorDrawable(color));
                if (failed) {
                    view.setHasFailed(true);
                }
            }
        }
    }

    private void setDownloadedDrawable(DownloadTask task) {
        WeakReference<DownloadImageView> imageViewWeakReference = task.getImageViewWeakReference();
        if (imageViewWeakReference != null) {
            DownloadImageView view = imageViewWeakReference.get();
            if (view != null) {
                view.setInCache(true);
                view.setDownloadActive(false);
                Bitmap bitmap = BitmapFactory.decodeFile(task.getResult().getAbsolutePath());
                view.setImageBitmap(bitmap);
            }
        }
    }

    public void startDownload(DownloadTask task) {
        mThreadPoolExecutor.execute(task.getDownloadRunnable());
    }

    public void stopDownload(DownloadTask task) {
        task.getCurrentThread().interrupt();
    }

    public void handleState(DownloadTask task, int state) {
        Message message = mHandler.obtainMessage(state, task);
        message.sendToTarget();

    }

}

