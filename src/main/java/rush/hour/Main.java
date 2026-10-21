package rush.hour;

import rush.hour.config.GameConfig;
import rush.hour.event.GameEvent;
import rush.hour.event.GameEventPublisher;
import rush.hour.event.GameEventType;
import rush.hour.event.listener.AuditLoggerListener;
import rush.hour.event.listener.ConsoleEventPrinter;
import rush.hour.event.listener.ErrorCounter;
import rush.hour.model.MapVO;
import rush.hour.service.exceptions.MapReaderException;
import rush.hour.service.map.parser.MapParser;
import rush.hour.service.map.printer.MapPrinter;
import rush.hour.service.map.reader.ScannerMapReader;


import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        GameConfig config = GameConfig.getInstance();

        GameEventPublisher publisher = new GameEventPublisher();
        ErrorCounter errorCounter = new ErrorCounter();
        publisher.subscribe(new AuditLoggerListener());
        publisher.subscribe(new ConsoleEventPrinter());
        publisher.subscribe(errorCounter);

        publisher.publish(new GameEvent(GameEventType.DEBUG, "Reading map from file"));
        Scanner mapScanner = new Scanner(Main.class.getClassLoader().getResourceAsStream(config.getBaseMapPath()));
        ScannerMapReader scannerMapReader = new ScannerMapReader(mapScanner);

        MapPrinter mapPrinter = new MapPrinter();
        try {


            List<String> rawMap = scannerMapReader.readMap();

            publisher.publish(new GameEvent(GameEventType.DEBUG, "Parsing map"));
            MapParser mapParser = new MapParser(config.getNumberOfRows() + config.getWallSize(),
                    config.getNumberOfColumns() + config.getWallSize());
            MapVO map = mapParser.parseMap(rawMap);

            publisher.publish(new GameEvent(GameEventType.DEBUG, "Printing map to console"));
            mapPrinter.printMap(map);

        } catch (MapReaderException e) {
            publisher.publish(new GameEvent(GameEventType.ERROR, e.getMessage()));
        }

        System.out.println(errorCounter.getErrorCount());

    }
}
