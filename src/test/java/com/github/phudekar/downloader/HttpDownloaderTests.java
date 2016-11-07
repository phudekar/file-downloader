package com.github.phudekar.downloader;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class HttpDownloaderTests {

    @Rule
    public WireMockRule service1 = new WireMockRule(8081);
    private HttpDownloader downloader;

    @Before
    public void before() {
        downloader = new HttpDownloader();
    }


    @Test
    public void shouldNotifyDownloadProgress() throws IOException {

        String filename = "test.txt";
        String url = "http://localhost:8081/" + filename;
        String location = ".";
        DownloadEntry downloadEntry = new DownloadEntry(url, location);

        stubFor(get(urlEqualTo("/" + filename))
                .willReturn(aResponse()
                        .withHeader("Content-Length","7")
                        .withBodyFile("test.txt")));

        TestProgressListener progressListener = new TestProgressListener();
        downloader.subscribeForNotification(progressListener);

        downloader.download(downloadEntry);

        assertThat(progressListener.getEntry(), is(downloadEntry));
        assertTrue(downloadEntry.getStatus().getDownloadedSize() > 0);
        assertTrue(downloadEntry.getStatus().isComplete());

        downloadEntry.getFile().delete();

    }

    @Test
    public void shouldDownloadFile() throws IOException {
        String filename = "test.txt";
        String url = "http://localhost:8081/" + filename;
        String location = ".";
        DownloadEntry downloadEntry = new DownloadEntry(url, location);

        stubFor(get(urlEqualTo("/" + filename))
                .willReturn(aResponse()
                        .withBodyFile("test.txt")));

        downloader.download(downloadEntry);

        File downloadedFile = downloadEntry.getFile();
        assertTrue(downloadedFile.exists());


        downloadedFile.delete();
    }

    class TestProgressListener implements ProgressListener {

        private DownloadEntry entry;

        @Override
        public void onProgress(DownloadEntry entry) {
            this.entry = entry;
        }

        public DownloadEntry getEntry() {
            return entry;
        }


    }
}
