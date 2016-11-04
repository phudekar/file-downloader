package com.github.phudekar.downloader.utils;

import com.github.phudekar.downloader.utils.FileNameParser;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class FileNameParserTests {

    @Test
    public void shouldReturnFileNameFromUrl(){
       String fileName = "abt.txt";
       String url = "http://foo.com/" + fileName;

        Optional<String> result = FileNameParser.getFileName(url);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(fileName));
    }

    @Test
    public void shouldReturnEmptyFileNameIfNotSpecifiedInUrl(){
        String url = "http://foo.com/bar/";

        Optional<String> result = FileNameParser.getFileName(url);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyFileName(){
        String url = "http://foo.com";

        Optional<String> result = FileNameParser.getFileName(url);

        assertFalse(result.isPresent());
    }


}

