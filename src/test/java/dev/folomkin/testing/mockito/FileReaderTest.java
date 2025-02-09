package dev.folomkin.testing.mockito;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileReaderTest {

    @Test
    void readFile() throws IOException {
        FileReader mockFileReader = mock(FileReader.class);
        when(mockFileReader.readFile(Mockito.anyString())).thenThrow(new IOException());

        FileProcessor mockFileProcessor = new FileProcessor(mockFileReader);
        String result = mockFileProcessor.processFile("test.txt");
        assertEquals("Error", result);
    }
}