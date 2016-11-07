package com.github.phudekar.downloader;

import com.github.phudekar.downloader.exceptions.InvalidCommandException;
import com.github.phudekar.downloader.utils.CommandParser;

public class Application {

    private static DownloadManager downloadManager;
    private static CommandParser commandParser;
    private static StringBuilder progressMessageBuffer = new StringBuilder();

    public static void main(String[] args) {

        HttpDownloader downloader = new HttpDownloader();
        downloader.subscribeForNotification(new ProgressListener() {
            @Override
            public void onProgress(DownloadEntry entry) {
                if (entry.getStatus().isComplete()) {
                    printProgress(entry);
                    System.out.println("\nFile downloaded successfully at " + entry.getFile().getAbsolutePath());
                } else {
                    printProgress(entry);
                }
            }
        });

        downloadManager = new DownloadManager(downloader);
        commandParser = new CommandParser();

        try {
            Command command = commandParser.parse(args);
            printStartDownloadMessage(command.getUrl());
            downloadManager.download(command.getUrl(), command.getLocation());
        } catch (InvalidCommandException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void printStartDownloadMessage(String url) {
        System.out.println("\nDownloading from " + url);
    }

    public static void printProgress(DownloadEntry entry) {
        DownloadStatus status = entry.getStatus();
        for (int i = 0; i < progressMessageBuffer.length(); i++) {
            System.out.print("\b");
        }
        progressMessageBuffer = new StringBuilder();

        progressMessageBuffer.append("[");

        for (int i = 1; i <= 10; i++) {
            if (status.getDownloadedSize() * 10 / status.getTotalSize() >= i)
                progressMessageBuffer.append("=");
            else
                progressMessageBuffer.append(".");
        }

        progressMessageBuffer.append("] ");
        progressMessageBuffer.append(Math.round(status.getDownloadedSize() / 1024) + "/" + Math.round(status.getTotalSize() / 1024) + " KB");
        System.out.print(progressMessageBuffer.toString());
    }

}
