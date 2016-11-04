package com.github.phudekar.downloader;

public interface ProgressListener {

    void onProgress(DownloadEntry entry, DownloadStatus status);

}
