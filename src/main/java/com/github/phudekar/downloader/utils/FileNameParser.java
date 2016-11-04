package com.github.phudekar.downloader.utils;

import com.github.phudekar.downloader.exceptions.InvalidUrlException;

import java.net.URI;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileNameParser {

    public static final String URL_SEPERATOR = "/";

    public static Optional<String> getFileName(String urlString) {
        try {
            String path = new URI(urlString).getPath();

            int lastIndexOfSeparator = path.lastIndexOf(URL_SEPERATOR);

            String fileName = lastIndexOfSeparator >= 0
                    ? path.substring(lastIndexOfSeparator).replace(URL_SEPERATOR, "").trim()
                    : "";

            if (fileName.length() == 0)
                return Optional.empty();

            return Optional.of(fileName);

        } catch (Exception e) {
            Logger.getLogger(FileNameParser.class.getName()).log(Level.SEVERE, e.getMessage());
            throw new InvalidUrlException(urlString);
        }
    }
}
