package com.github.phudekar.downloader;

import com.github.phudekar.downloader.exceptions.InvalidUrlException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
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
            long totalBytesRead = entry.getFile().length();
            String range = "bytes=" + totalBytesRead + "-";
            connection.setRequestProperty("Range", range);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {

                long totalSize = totalBytesRead + connection.getContentLengthLong();
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(entry.getFile(),true);

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) > -1 && !Thread.currentThread().isInterrupted()) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                    totalBytesRead += bytesRead;
                    this.notifyProgress(totalBytesRead, totalSize);
                }

                connection.disconnect();
                outputStream.close();
            } else {
                throw new ConnectException("Received response " + responseCode);
            }
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(entry.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyProgress(long sizeDownloaded, long totalSize) {
        entry.updateStatus(new DownloadStatus(totalSize, sizeDownloaded));
        this.progressListeners.stream().forEach(progressListener -> progressListener.onProgress(entry));
    }

    public void subscribeForNotification(ProgressListener progressListener) {
        this.progressListeners.add(progressListener);
    }
}
