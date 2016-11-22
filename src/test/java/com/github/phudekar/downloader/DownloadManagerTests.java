package com.github.phudekar.downloader;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class DownloadManagerTests {

    final String fileName = "README.md";
    String url = "https://raw.githubusercontent.com/phudekar/file-downloader/master/" + fileName;
    final String location = ".";
    private DownloadManager downloadManager;

    @Rule
    public WireMockRule service = new WireMockRule(8081);
    private Downloader downloader;

    @Before
    public void beforeEach() {
        downloader = mock(Downloader.class);
        downloadManager = new DownloadManager(downloader);
    }

    @Test
    public void shouldCreateDownloadEntryForFile() {
        DownloadEntry downloadEntry = new DownloadEntry(url, location);
        downloadManager.download(downloadEntry);

        assertThat(downloadEntry.getUrl(), is(url));
        assertThat(downloadEntry.getFile().getPath(), is(location + "/" + fileName));
    }

    @Test
    public void shouldReturnDownloadEntries() {
        DownloadEntry downloadEntry = new DownloadEntry(url, location);
        downloadManager.download(downloadEntry);

        assertThat(downloadManager.getDownloads().size(), is(1));
        assertThat(downloadManager.getDownloads().stream().findFirst().get(), is(downloadEntry));
    }

}
