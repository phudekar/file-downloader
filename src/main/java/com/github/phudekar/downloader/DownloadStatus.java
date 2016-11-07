package com.github.phudekar.downloader;

public class DownloadStatus {

    private double totalSize;
    private double downloadedSize;

    public DownloadStatus(double totalSize, double downloadedSize) {
        this.totalSize = totalSize;
        this.downloadedSize = downloadedSize;
    }

    public double getTotalSize() {
        return totalSize;
    }

    public double getDownloadedSize() {
        return downloadedSize;
    }

    public boolean isComplete() {
        return getTotalSize() > 0 && getDownloadedSize() >= getTotalSize();
    }
}
