package dev.folomkin.testing.mockito;

import java.io.IOException;

public class FileProcessor {
    private FileReader fileReader;

    public FileProcessor(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public String processFile(String path) {
        try {
            return "Processed: " + fileReader.readFile(path);
        } catch (IOException e) {
            return "Error";
        }
    }
}
