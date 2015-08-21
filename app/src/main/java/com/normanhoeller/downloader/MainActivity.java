package com.normanhoeller.downloader;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import com.normanhoeller.downloader.ui.GridFragment;
import com.normanhoeller.downloader.utils.DownloadImageView;

import java.io.File;


public class MainActivity extends Activity implements GridFragment.ActivityCallback {

    private DownlaodService mService;

    private boolean isBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            GridFragment fragment = new GridFragment();
            getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, DownlaodService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_cache) {
            clearCache();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearCache() {
        File dir = getExternalCacheDir();
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }

        Fragment fragment = getFragmentManager().findFragmentById(android.R.id.content);
        if (fragment != null && fragment instanceof GridFragment) {
            ((GridFragment) fragment).refreshAdapterAfterCleaningStorage();
        }
    }

    @Override
    public boolean isBound() {
        return isBound;
    }

    @Override
    public void startDownload(String url, DownloadImageView imageView) {
        mService.downlaodBitmap(url, imageView);
    }

    @Override
    public void stopDownLoad(String url) {
        mService.stopDownload(url);
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DownlaodService.LocalService binder = (DownlaodService.LocalService) service;
            mService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
}
