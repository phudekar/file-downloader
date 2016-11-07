package com.github.phudekar.downloader;

import java.util.HashSet;
import java.util.Set;

public class DownloadManager {

    final HashSet<DownloadEntry> downloads = new HashSet<>();
    private Downloader downloader;

    public DownloadManager(Downloader downloader) {
        this.downloader = downloader;
    }

    public DownloadEntry download(String url, String location) {
        DownloadEntry entry = new DownloadEntry(url, location);
        downloads.add(entry);
        downloader.download(entry);
        return entry;
    }

    public Set<DownloadEntry> getDownloads() {
        return this.downloads;
    }

}
