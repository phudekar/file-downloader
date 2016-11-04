package com.github.phudekar.downloader;

import com.github.phudekar.downloader.exceptions.InvalidCommandException;
import com.github.phudekar.downloader.utils.CommandParser;

public class Application {

    private static DownloadManager downloadManager;
    private static CommandParser commandParser;

    public static void main(String[] args) {

        HttpDownloader downloader = new HttpDownloader();
        downloader.subscribeForNotification(new ProgressListener() {
            @Override
            public void onProgress(DownloadEntry entry) {
                if (entry.getStatus().isComplete()) {
                    System.out.println("\nFile downloaded successfully at " + entry.getFile().getAbsolutePath());
                } else {
                    printProgress(entry.getStatus());
                }
            }
        });

        downloadManager = new DownloadManager(downloader);
        commandParser = new CommandParser();

        try {
            Command command = commandParser.parse(args);
            downloadManager.download(command.getUrl(), command.getLocation());
        } catch (InvalidCommandException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void printProgress(DownloadStatus status) {

        for (int i = 0; i < 12; i++) {
            System.out.print("\b");
        }

        System.out.println("[");

        for (int i = 1; i <= 10; i++) {
            if (status.getDownloadedSize() * 10 / status.getTotalSize() >= i)
                System.out.print("=");
            else
                System.out.print(".");
        }

        System.out.println("]");


    }

}
