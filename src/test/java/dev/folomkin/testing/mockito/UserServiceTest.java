package dev.folomkin.testing.mockito;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void whenUsingAnyMatcher_thenUserIsReturned() {
        UserService mockUserService = mock(UserService.class);
        mockUserService.findUser("Alice", 30);

        verify(mockUserService).findUser(eq("Alice"), eq(30));
    }
}