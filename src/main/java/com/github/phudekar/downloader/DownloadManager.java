package com.github.phudekar.downloader;

import java.util.HashMap;
import java.util.Optional;

public class DownloadManager implements ProgressListener {

    final HashMap<DownloadEntry, Optional<DownloadStatus>> downloads = new HashMap<>();
    private Downloader downloader;

    public DownloadManager(Downloader downloader) {
        this.downloader = downloader;
        downloader.subscribeForNotification(this);
    }

    public DownloadEntry download(String url, String location) {
        DownloadEntry entry = new DownloadEntry(url, location);
        downloads.put(entry, Optional.empty());
        downloader.download(entry);
        return entry;
    }

    @Override
    public void onProgress(DownloadEntry entry, DownloadStatus status) {
        downloads.put(entry, Optional.of(status));
    }

    public HashMap<DownloadEntry, Optional<DownloadStatus>> getDownloads() {
        return this.downloads;
    }

}
