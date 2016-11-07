package com.github.phudekar.downloader.utils;

import com.github.phudekar.downloader.Command;
import com.github.phudekar.downloader.exceptions.InvalidCommandException;

public class CommandParser {


    private static final String DEFAULT_LOCATION = ".";

    public Command parse(String[] args) throws InvalidCommandException {
        if (args.length == 0) {
            throw new InvalidCommandException("URL Missing from arguments");
        }

        String url = args[0].trim();
        String location = DEFAULT_LOCATION;
        if (args.length > 1) {
            location = args[1].trim();
        }
        return new Command(url, location);
    }
}
