File Downloader
===============

This is simple command line utility to download a file from a url.

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