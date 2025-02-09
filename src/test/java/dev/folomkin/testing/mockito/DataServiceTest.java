package dev.folomkin.testing.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

    @Mock
    DataService mockDataService;

    @InjectMocks
    DataProcessor mockDataProcessor;

    @Test
    public void testRetrieveData() {
        when(mockDataService.retrieveData()).thenReturn(5.0);
        assertEquals(10.0, mockDataProcessor.processData());
    }

}