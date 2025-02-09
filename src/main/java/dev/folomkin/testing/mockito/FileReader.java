package dev.folomkin.testing.mockito;

import java.io.IOException;

public interface FileReader {
    String readFile(String path) throws IOException;
}
