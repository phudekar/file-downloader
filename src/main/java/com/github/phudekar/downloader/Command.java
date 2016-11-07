package com.github.phudekar.downloader;

public class Command {

    private String url;
    private String location;

    public Command(String url, String location) {

        this.url = url;
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public String getLocation() {
        return location;
    }
}
