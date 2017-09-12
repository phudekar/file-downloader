package com.github.phudekar.downloader;

import java.io.IOException;

public interface Downloader {

    void download(DownloadEntry entry) throws IOException;

    void subscribeForNotification(ProgressListener progressListener);

}
