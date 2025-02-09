package dev.folomkin.testing.mockito;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Test
    void verifyMethodCall() {

        //Создание мока
        OrderService orderService = mock(OrderService.class);
        orderService.placeOrder(new Order("Item1", 1));
        orderService.placeOrder(new Order("Item2", 2));

        verify(orderService, times(2)).placeOrder(Mockito.any(Order.class));

        verify(orderService, never()).getOrderCount();

    }
}