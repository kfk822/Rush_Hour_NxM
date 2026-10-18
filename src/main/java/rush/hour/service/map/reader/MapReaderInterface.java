package rush.hour.service.map.reader;

import rush.hour.service.exceptions.MapReaderException;

import java.util.List;

public interface MapReaderInterface {
    List<String> readMap() throws MapReaderException;
}
