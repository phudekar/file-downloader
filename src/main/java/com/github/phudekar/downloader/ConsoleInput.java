package com.github.phudekar.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInput {

    public static final char PAUSE = 'p';
    public static final char RESUME = 'r';

    public void listenForCommands(DownloadEntry entry, DownloadManager downloadManager, Runnable onComplete) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            char[] buffer = new char[1];

            while (!(entry.getStatus().isComplete() || entry.getStatus().hasFailed())) {
                if (reader.ready() && reader.read(buffer) > 0) {
                    char action = buffer[0];
                    if (action == PAUSE && !downloadManager.isPaused(entry)) {
                        downloadManager.pause(entry);
                        System.out.println("Enter '" + RESUME + "' to resume.");
                    } else if (action == RESUME && downloadManager.isPaused(entry)) {
                        downloadManager.resume(entry);
                        clearLine();
                    }
                }
            }

            reader.close();
            onComplete.run();

            if(entry.getStatus().hasFailed())
                System.exit(1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearLine() {
        System.out.print("\033[2K"); // clear line
        for (int i = 0; i < 3; i++) {
            System.out.print(String.format("\033[%dA", 1)); // move line up
            System.out.print("\033[2K");
        }
    }
}
