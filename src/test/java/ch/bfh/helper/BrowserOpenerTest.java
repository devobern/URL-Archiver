package ch.bfh.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class BrowserOpenerTest {

    @Test
    void openURLWithValidURL() {
        // It's hard to verify browser opening, but no exceptions should be thrown for a valid URL
        assertDoesNotThrow(() -> BrowserOpener.openURL("http://example.com"));
    }

    @Test
    void openURLWhenDesktopNotSupported() {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class)) {
            mockedDesktop.when(Desktop::isDesktopSupported).thenReturn(false);

            Executable action = () -> BrowserOpener.openURL("http://example.com");
            assertThrows(UnsupportedOperationException.class, action);
        }
    }

    @Test
    void openURLWhenBrowseActionNotSupported() {
        try (MockedStatic<Desktop> mockedDesktop = Mockito.mockStatic(Desktop.class)) {
            mockedDesktop.when(Desktop::isDesktopSupported).thenReturn(true);

            Desktop mockDesktopInstance = Mockito.mock(Desktop.class);
            Mockito.when(mockDesktopInstance.isSupported(Desktop.Action.BROWSE)).thenReturn(false);
            mockedDesktop.when(Desktop::getDesktop).thenReturn(mockDesktopInstance);

            Executable action = () -> BrowserOpener.openURL("http://example.com");
            assertThrows(UnsupportedOperationException.class, action);
        }
    }
}
