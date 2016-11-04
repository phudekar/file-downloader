package com.github.phudekar.downloader;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DownloadStatusTests {

    @Test
    public void shouldReturnInCompleteIfTotalAndDownloadedSizeAreSame(){
        DownloadStatus status = new DownloadStatus(10, 5);

        assertFalse(status.isComplete());
    }

    @Test
    public void shouldReturnCompleteIfTotalAndDownloadedSizeAreSame(){
        DownloadStatus status = new DownloadStatus(10, 10);

        assertTrue(status.isComplete());
    }
}
