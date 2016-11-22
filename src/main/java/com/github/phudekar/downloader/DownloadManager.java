package com.github.phudekar.downloader;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DownloadManager {

    final HashMap<DownloadEntry, Future> downloads = new HashMap<>();
    private Downloader downloader;
    ExecutorService executor = Executors.newCachedThreadPool();

    public DownloadManager(Downloader downloader) {
        this.downloader = downloader;
    }

    public void download(DownloadEntry entry) {
        if (!downloads.containsKey(entry))
            downloads.put(entry, executor.submit(() -> downloader.download(entry)));
    }

    public Set<DownloadEntry> getDownloads() {
        return this.downloads.keySet();
    }

    public void pause(DownloadEntry entry) {
        if (this.downloads.containsKey(entry))
            this.downloads.get(entry).cancel(true);
    }

    public void resume(DownloadEntry entry) {
        if (downloads.containsKey(entry))
            downloads.put(entry, executor.submit(() -> downloader.download(entry)));
    }

    public boolean isPaused(DownloadEntry entry) {
        return this.downloads.containsKey(entry) && this.downloads.get(entry).isCancelled();
    }

}
