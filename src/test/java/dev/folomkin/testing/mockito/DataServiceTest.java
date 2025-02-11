package dev.folomkin.testing.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {


    @Test
    void dataServiceTest() {
        DataService dataService = Mockito.mock(DataService.class);
        DataService dataServiceSpy = Mockito.spy(DataService.class);

        List<String> data = new ArrayList<>();
        data.add("dataItem");
        Mockito.when(dataService.getData()).thenReturn(data);
        Mockito.doReturn(data).when(dataServiceSpy).getData();

        Mockito.when(dataService.getDataById("itnValue")).thenReturn("dataItem");

    }
}