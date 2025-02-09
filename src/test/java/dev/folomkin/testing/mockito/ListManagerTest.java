package dev.folomkin.testing.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ListManagerTest {

    @Spy
    ListManager spyListManager;

    @Test
    void spyExample() {

        List<String> list = new ArrayList<>(Arrays.asList("one", "two", "three"));
        doReturn(100).when(spyListManager).getListSize(list);
        assertEquals(100, spyListManager.getListSize(list));
    }
}