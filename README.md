File Downloader
===============

[![Build Status](https://travis-ci.org/phudekar/file-downloader.svg?branch=master)](https://travis-ci.org/phudekar/file-downloader)

This is simple command line utility to download a file from a url. 
You can pause the active download by entering `p` and resume it by entering `r`.

If there exists a file with same name on output path, it will replace that file.

## PRE-REQUISITES

- JDK 1.8

## Running tests
```
gradle test

```

## Build the application
```
gradle build

```

This will create a distributable bundle of the application.

### Extract the bundle

```
tar -xvf build/distributions/file-downloader-1.0-SNAPSHOT.tar

cd file-downloader-1.0-SNAPSHOT/bin/

```


## Run the application

```
./file-downloader "<http-url>" <location>

```