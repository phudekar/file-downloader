package com.github.phudekar.downloader.exceptions;

public class InvalidCommandException extends Throwable {
    public InvalidCommandException(String message) {
        super("Cannot parse arguments: " + message);
    }
}
