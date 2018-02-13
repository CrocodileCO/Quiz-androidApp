package com.crocodile.quiz.helper;


import android.graphics.Bitmap;

public class DownloadHelper {

    public static void downloadImage(String url, OnImageDownloadListener listener) {
        new DownloadImageTask(listener).execute(url);
    }


    public interface OnImageDownloadListener {
        void onImageDownloaded(Bitmap image);
    }
}
