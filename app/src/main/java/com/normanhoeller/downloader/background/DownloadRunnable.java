package com.normanhoeller.downloader.background;

import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by norman on 19/09/14.
 * runnable holding code for downloading a resource over http will be executed in a {@link java.util.concurrent.ThreadPoolExecutor}
 */
public class DownloadRunnable implements Runnable {

    private static final String TAG = DownloadRunnable.class.getSimpleName();
    private DownloadTask mDownloadTask;

    public DownloadRunnable(DownloadTask task) {
        mDownloadTask = task;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mDownloadTask.setCurrentThread(Thread.currentThread());

         /*
         * A try block that downloads a resource from a URL.
         */
        try {
            if (Thread.interrupted()) {

                throw new InterruptedException();
            }

            mDownloadTask.handleState(DownloadManager.DOWNLOAD_STARTED);
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            String fileURL = mDownloadTask.getUrlString();

            try {

                URL url = new URL(mDownloadTask.getUrlString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (Thread.interrupted()) {

                    throw new InterruptedException();
                }

                inputStream = connection.getInputStream();

                if (Thread.interrupted()) {

                    throw new InterruptedException();
                }


                int responseCode = connection.getResponseCode();

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String fileName = "";
//                    String disposition = connection.getHeaderField("Content-Disposition");
                    String disposition = null;
                    String contentType = connection.getContentType();
                    int contentLength = connection.getContentLength();

                    if (disposition != null) {
                        // extracts file name from header field
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            fileName = disposition.substring(index + 10,
                                    disposition.length() - 1);
                        }
                    } else {
                        // extracts file name from URL
                        fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                                fileURL.length());
                    }

                    Log.d(TAG, "Content-Type = " + contentType);
                    Log.d(TAG, "Content-Disposition = " + disposition);
                    Log.d(TAG, "Content-Length = " + contentLength);
                    Log.d(TAG, "fileName = " + fileName);

                    String saveFilePath = mDownloadTask.getSaveDestination() + File.separator + fileName;

                    // opens an output stream to save into file
                    outputStream = new FileOutputStream(saveFilePath);

                    int bytesRead = -1;
                    byte[] buffer = new byte[DownloadTask.BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);

                        if (Thread.interrupted()) {

                            throw new InterruptedException();
                        }

                    }
                    mDownloadTask.setResult(new File(saveFilePath));
                    mDownloadTask.handleState(DownloadManager.DOWNLOAD_COMPLETED);
                } else {
                    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                    mDownloadTask.handleState(DownloadManager.DOWNLOAD_FAILED);
                }
                connection.disconnect();

            } catch (IOException e) {
                mDownloadTask.handleState(DownloadManager.DOWNLOAD_FAILED);
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {

                    }
                }

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {

                    }
                }
            }
        } catch (InterruptedException e) {
            mDownloadTask.handleState(DownloadManager.DOWNLOAD_INTERRUPTED);
        } finally {
            // Sets the reference to the current Thread to null, releasing its storage
            mDownloadTask.setCurrentThread(null);

            // Clears the Thread's interrupt flag
            Thread.interrupted();
        }
    }
}