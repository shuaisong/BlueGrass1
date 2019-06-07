package com.reeching.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import com.reeching.listener.ScanListener;

import java.io.File;

public class MediaScanner {

    private static final String TAG = MediaScanner.class.getSimpleName();

    private MediaScannerConnection mConn = null;
    private File mFile = null;
    private String mMimeType = null;

    public MediaScanner(Context context) {
        SannerClient client;
        client = new SannerClient();
        if (mConn == null) {
            mConn = new MediaScannerConnection(context, client);
        }
    }

    class SannerClient implements
            MediaScannerConnection.MediaScannerConnectionClient {

        public void onMediaScannerConnected() {

            if (mFile == null) {
                return;
            }
            scan(mFile, mMimeType);
        }

        public void onScanCompleted(String path, Uri uri) {
            mConn.disconnect();
            if (mScanListener != null) {
                mScanListener.onScanCompleted();
            }
            Log.i(TAG, "scan done " + path);
        }

        private void scan(File file, String type) {
            Log.i(TAG, "scan " + file.getAbsolutePath());
            if (file.isFile()) {
                mConn.scanFile(file.getAbsolutePath(), type);
                return;
            }
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File f : file.listFiles()) {
                scan(f, type);
            }
        }
    }

    public void scanFile(File file, String mimeType, ScanListener listener) {
        mFile = file;
        mMimeType = mimeType;
        mConn.connect();
        mScanListener = listener;
    }

    private ScanListener mScanListener;


}
