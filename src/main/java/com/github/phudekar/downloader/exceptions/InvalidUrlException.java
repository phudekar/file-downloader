package com.github.phudekar.downloader.exceptions;

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String url){
        super("Can not parse url: " + url);
    }
}
