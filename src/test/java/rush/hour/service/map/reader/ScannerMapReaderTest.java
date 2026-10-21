package rush.hour.service.map.reader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.service.exceptions.MapReaderException;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScannerMapReaderTest {

    private ScannerMapReader scannerMapReader;

    @Test
    @DisplayName("Should return list of map lines in correct order when input content is valid")
    void testReadMapShouldReturnLinesWhenInputIsValid() throws MapReaderException {
        String fakeMapContent = "########\n#..pP..#\n########";
        Scanner scanner = new Scanner(fakeMapContent);
        scannerMapReader = new ScannerMapReader(scanner);

        List<String> result = scannerMapReader.readMap();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("########", result.get(0));
        assertEquals("#..pP..#", result.get(1));
        assertEquals("########", result.get(2));
    }

    @Test
    @DisplayName("Should throw MapReaderException when input scanner contains no content")
    void testReadMapShouldThrowMapReaderExceptionWhenInputIsEmpty() {
        Scanner emptyScanner = new Scanner("");
        scannerMapReader = new ScannerMapReader(emptyScanner);

        assertThrows(MapReaderException.class, () -> scannerMapReader.readMap());
    }
}