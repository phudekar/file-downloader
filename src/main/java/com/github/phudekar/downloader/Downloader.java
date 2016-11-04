package com.github.phudekar.downloader;

public interface Downloader {

    void download(DownloadEntry entry);

    void subscribeForNotification(ProgressListener progressListener);

}
