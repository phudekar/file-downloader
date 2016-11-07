package com.github.phudekar.downloader;

import com.github.phudekar.downloader.exceptions.InvalidUrlException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpDownloader implements Downloader {
    private static final int BUFFER_SIZE = 4096;

    private List<ProgressListener> progressListeners = new ArrayList<>();
    private DownloadEntry entry;

    @Override
    public void download(DownloadEntry entry) {
        this.entry = entry;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(entry.getUrl()).openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                long contentLength = connection.getContentLengthLong();
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(entry.getFile());

                byte[] buffer = new byte[BUFFER_SIZE];
                int totalBytesRead = 0;
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) > -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    this.notifyProgress(totalBytesRead, contentLength);
                }

                connection.disconnect();

                outputStream.flush();
                outputStream.close();
            }
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(entry.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyProgress(double sizeDownloaded, double totalSize) {
        entry.updateStatus(new DownloadStatus(totalSize, sizeDownloaded));
        this.progressListeners.stream().forEach(progressListener -> progressListener.onProgress(entry));
    }

    public void subscribeForNotification(ProgressListener progressListener) {
        this.progressListeners.add(progressListener);
    }
}
