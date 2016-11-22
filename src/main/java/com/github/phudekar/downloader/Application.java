package com.github.phudekar.downloader;

import com.github.phudekar.downloader.exceptions.InvalidCommandException;
import com.github.phudekar.downloader.utils.CommandParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {

    private static final char PAUSE = 'p';
    private static final char RESUME = 'r';
    private static DownloadManager downloadManager;
    private static CommandParser commandParser;
    private static StringBuilder messageBuilder = new StringBuilder();


    public static void main(String[] args) {

        HttpDownloader downloader = new HttpDownloader();
        downloadManager = new DownloadManager(downloader);
        commandParser = new CommandParser();

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

        try {
            Command command = commandParser.parse(args);
            printStartDownloadMessage(command.getUrl());

            DownloadEntry entry = new DownloadEntry(command.getUrl(), command.getLocation());
            downloadManager.download(entry);
            listenForCommands(entry);

        } catch (InvalidCommandException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private static void listenForCommands(DownloadEntry entry) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            char[] buffer = new char[1];
            while (true) {
                if (entry.getStatus().isComplete()) {
                    break;
                }

                if (reader.ready() && reader.read(buffer) > 0) {
                    char action = buffer[0];
                    if (action == PAUSE) {
                        downloadManager.pause(entry);
                        System.out.println("PAUSED (Press '" + RESUME + "' to resume)");
                    } else if (action == RESUME) {
                        downloadManager.download(entry);
                        clearScreen();
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printStartDownloadMessage(String url) {
        System.out.println("\nDownloading from " + url);
        System.out.println("Press '" + PAUSE + "' to Pause and '" + RESUME + "' to resume.");
    }

    private static void printProgress(DownloadEntry entry) {
        DownloadStatus status = entry.getStatus();
        for (int i = 0; i < messageBuilder.length() + 5; i++) {
            System.out.print("\b");
        }
        messageBuilder = new StringBuilder();
        messageBuilder.append("[");

        for (int i = 1; i <= 10; i++) {
            if (status.getDownloadedSize() * 10 / status.getTotalSize() >= i)
                messageBuilder.append("=");
            else
                messageBuilder.append(".");
        }

        messageBuilder.append("] ");
        messageBuilder.append(Math.round(status.getDownloadedSize() / 1024) + "/" + Math.round(status.getTotalSize() / 1024) + " KB");
        System.out.print(messageBuilder.toString());
    }

    private static void clearScreen() {
        for (int i = 0; i < 3; i++) {
            System.out.print("\033[2K"); // Erase line content
            System.out.print(String.format("\033[%dA", 1)); // Move up
        }
    }

}
