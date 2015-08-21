package com.normanhoeller.downloader.ui;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.normanhoeller.downloader.R;
import com.normanhoeller.downloader.adapter.SimpleAdapter;
import com.normanhoeller.downloader.utils.DownloadImageView;
import com.normanhoeller.downloader.utils.Thumbnail;

import java.io.File;

/**
 * Created by norman on 19/09/14.
 * A {@link android.app.Fragment} showing some Items. Clicking on them will start or - if download is already running - stop a download.
 */
public class GridFragment extends Fragment {

    private static final String TAG = GridFragment.class.getSimpleName();

    private GridView mGridView;
    private SimpleAdapter mAdapter;
    private ActivityCallback mActivityCallback;

    public interface ActivityCallback {
        boolean isBound();

        void startDownload(String url, DownloadImageView imageView);

        void stopDownLoad(String url);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mActivityCallback = (ActivityCallback) activity;
        } catch (ClassCastException e) {
            Log.d(TAG, "ClassCastException " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGridView = (GridView) getView().findViewById(R.id.grid_view);
        mAdapter = new SimpleAdapter(getActivity());

        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DownloadImageView imageView = (DownloadImageView) view.findViewById(R.id.image_view);
                Thumbnail thumbnail = (Thumbnail) mAdapter.getItem(position);
//              an example of how to check whether a file is already there or not. Most probably we would use an sqlite database to and a contentprovider.
//              this implementation kills my primitive stopping mechanism - so I uncomment it. 
//                Bitmap bitmap = isFileAlraedyDownloaded(thumbnail.getUrl());
//                if (bitmap != null) {
//                    imageView.setImageBitmap(bitmap);
//                    return;
//                }

                if (mActivityCallback != null && mActivityCallback.isBound()) {
                    if (imageView.isDownloadActive() && !imageView.isHasFailed()) {
                        imageView.setDownloadActive(false);
                        mActivityCallback.stopDownLoad(thumbnail.getUrl());
                    } else if (!imageView.isInCache()) {
                        imageView.setDownloadActive(true);
                        mActivityCallback.startDownload(thumbnail.getUrl(), imageView);
                    }
                }
            }
        });
    }

    private Bitmap isFileAlraedyDownloaded(String fileURL) {
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                fileURL.length());
        String saveFilePath = getActivity().getExternalCacheDir() + File.separator + fileName;
        File file = new File(saveFilePath);
        if (file.exists()) {
            return showFileFromCache(file);
        }
        return null;
    }

    private Bitmap showFileFromCache(File file) {
//        in a real application this would not happen on the ui thread
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public void refreshAdapterAfterCleaningStorage() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }
}
