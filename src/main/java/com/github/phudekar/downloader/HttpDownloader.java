package com.github.phudekar.downloader;

import com.github.phudekar.downloader.exceptions.InvalidUrlException;
import com.github.phudekar.downloader.exceptions.UnexpectedResponseException;

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

    @Override
    public void download(DownloadEntry entry) {
        try {
            downloadFromUrl(entry, entry.getUrl());
        } catch (UnexpectedResponseException e) {
            if (e.getResponseCode() == 302 || e.getResponseCode() == 301) {
                try {
                    System.out.println("Received 302. Trying again with " + e.getLocation());
                    downloadFromUrl(entry, e.getLocation());
                } catch (UnexpectedResponseException e1) {
                   System.out.println("Could not download file from : " + e.getLocation());
                }
            }
        }
    }

    private void downloadFromUrl(DownloadEntry entry, String url) throws UnexpectedResponseException {
        HttpURLConnection connection = null;
        FileOutputStream outputStream = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            long totalBytesRead = entry.getFile().length();
            connection.setRequestProperty("Range", getRangeHeader(totalBytesRead));
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {

                long totalSize = totalBytesRead + connection.getContentLengthLong();
                InputStream inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(entry.getFile(), true);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) > -1 && !Thread.currentThread().isInterrupted()) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                    totalBytesRead += bytesRead;
                    this.notifyProgress(entry, totalBytesRead, totalSize);
                }

            } else {
                throw new UnexpectedResponseException(responseCode, connection.getHeaderField("Location"));
            }
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(entry.getUrl());
        } catch (IOException e) {
            System.out.println("Failed to download file. " + e.getMessage());
        } finally {
            if (connection != null)
                connection.disconnect();

            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private String getRangeHeader(long offset) {
        return "bytes=" + offset + "-";
    }

    private void notifyProgress(DownloadEntry entry, long sizeDownloaded, long totalSize) {
        entry.updateStatus(new DownloadStatus(totalSize, sizeDownloaded));
        this.progressListeners.stream().forEach(progressListener -> progressListener.onProgress(entry));
    }

    public void subscribeForNotification(ProgressListener progressListener) {
        this.progressListeners.add(progressListener);
    }
}
