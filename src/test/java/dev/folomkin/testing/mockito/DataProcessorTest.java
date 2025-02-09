package dev.folomkin.testing.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DataProcessorTest {

    @Mock
    DataService mockDataService;

    @Test
    void testProcessData() {
        when(mockDataService.retrieveData()).thenReturn(5.0);
        DataProcessor dataProcessor = new DataProcessor(mockDataService);
        assertEquals(10.0, dataProcessor.processData());
    }
}