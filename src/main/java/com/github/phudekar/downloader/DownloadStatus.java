package com.github.phudekar.downloader;

public class DownloadStatus {

    private long totalSize;
    private long downloadedSize;
    private boolean failed;

    public DownloadStatus(long totalSize, long downloadedSize) {
        this.totalSize = totalSize;
        this.downloadedSize = downloadedSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public boolean isComplete() {
        return getTotalSize() > 0 && getDownloadedSize() >= getTotalSize();
    }

    public boolean hasFailed() {
        return this.failed;
    }

    public void failed() {
        this.failed = true;
    }
}
