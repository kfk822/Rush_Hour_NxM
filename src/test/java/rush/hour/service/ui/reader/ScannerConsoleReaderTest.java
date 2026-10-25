package rush.hour.service.ui.reader;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScannerConsoleReaderTest {

    @Test
    void testReadShouldReturnNextLineFromScanner() {
        Scanner scanner = new Scanner("testUser\n");
        ScannerConsoleReader reader = new ScannerConsoleReader(scanner);

        String actual = reader.read();

        assertEquals("testUser", actual);
    }
}