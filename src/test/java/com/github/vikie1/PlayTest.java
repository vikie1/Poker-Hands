package com.github.vikie1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PlayTest {
    @Test
    void main_checkForFileIOException_appStartsSuccessfully() throws IOException {
        Play.main(new String[]{"no args"});
    }
}