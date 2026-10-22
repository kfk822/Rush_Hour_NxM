package rush.hour.GameEngine;

import rush.hour.Main;
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

public class RushHour implements Game{

    private boolean isRunning = true;

    GameConfig config;

    GameEventPublisher publisher;
    ErrorCounter errorCounter;

    Scanner mapScanner;
    ScannerMapReader scannerMapReader;

    MapPrinter mapPrinter;
    MapParser mapParser;
    MapVO map;

    @Override
    public void init(){
        config = GameConfig.getInstance();

        publisher = new GameEventPublisher();
        errorCounter = new ErrorCounter();
        publisher.subscribe(new AuditLoggerListener());
        publisher.subscribe(new ConsoleEventPrinter());
        publisher.subscribe(errorCounter);

        publisher.publish(new GameEvent(GameEventType.DEBUG, "Reading map from file"));
        mapScanner = new Scanner(Main.class.getClassLoader().getResourceAsStream(config.getBaseMapPath()));
        scannerMapReader = new ScannerMapReader(mapScanner);

        mapPrinter = new MapPrinter();

        try {
            List<String> rawMap = scannerMapReader.readMap();

            publisher.publish(new GameEvent(GameEventType.DEBUG, "Parsing map"));
            mapParser = new MapParser(config.getNumberOfRows() + config.getWallSize(),
                    config.getNumberOfColumns() + config.getWallSize());
            map = mapParser.parseMap(rawMap);

        } catch (MapReaderException e) {
            publisher.publish(new GameEvent(GameEventType.ERROR, e.getMessage()));
        }
    }
    @Override
    public void processInput(){

    }
    @Override
    public void update(){
        isRunning = false;
    }
    @Override
    public void render(){
        mapPrinter.printMap(map);
    }
    @Override
    public void terminate(){
        System.out.println(errorCounter.getErrorCount());
    }
    @Override
    public boolean getIsRunning() {
        return isRunning;
    }
}
