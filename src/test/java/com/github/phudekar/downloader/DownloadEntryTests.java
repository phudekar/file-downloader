package com.github.phudekar.downloader;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DownloadEntryTests {

    @Test
    public void shouldSetDefaultFilenameForDownloadEntry() {
        String url = "https://raw.githubusercontent.com/phudekar/file-downloader/master/";
        String location = ".";

        DownloadEntry downloadEntry = new DownloadEntry(url, location);

        assertThat(downloadEntry.getFile().getPath(), is(location + "/" + DownloadEntry.DEFAULT_FILE_NAME));
    }


}
