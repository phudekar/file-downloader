package com.github.phudekar.downloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DownloadManager {

    ExecutorService executor;

    final HashMap<DownloadEntry, Future> downloads = new HashMap<>();
    private Downloader downloader;


    public DownloadManager(Downloader downloader, ExecutorService executor) {
        this.downloader = downloader;
        this.executor = executor;
    }

    public void download(DownloadEntry entry) {
        if (!downloads.containsKey(entry))
            downloads.put(entry, executor.submit(() -> {
                try {
                    downloader.download(entry);
                } catch (IOException e) {
                  onError(entry, e.getMessage());
                }
            }));
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
            downloads.put(entry, executor.submit(() -> {
                try {
                    downloader.download(entry);
                } catch (IOException e) {
                    onError(entry, e.getMessage());
                }
            }));
    }

    public boolean isPaused(DownloadEntry entry) {
        return this.downloads.containsKey(entry) && this.downloads.get(entry).isCancelled();
    }

    private void onError(DownloadEntry entry, String message){
        System.out.println("Failed to download file. " + message);
        entry.getStatus().failed();
    }
}
