package rush.hour.GameEngine;

import rush.hour.Main;
import rush.hour.config.GameConfig;
import rush.hour.event.GameEvent;
import rush.hour.event.GameEventPublisher;
import rush.hour.event.GameEventType;
import rush.hour.event.listener.AuditLoggerListener;
import rush.hour.event.listener.ConsoleEventPrinter;
import rush.hour.event.listener.ErrorCounter;
import rush.hour.model.*;
import rush.hour.service.enemy.EnemyAI;
import rush.hour.service.exceptions.MapReaderException;
import rush.hour.service.exceptions.MoveMakerException;
import rush.hour.service.exceptions.MovementValidatorException;
import rush.hour.service.map.parser.MapParser;
import rush.hour.service.map.printer.MapPrinter;
import rush.hour.service.map.reader.ScannerMapReader;
import rush.hour.service.map.validator.MovementValidator;
import rush.hour.service.move.MoveMaker;
import rush.hour.service.ui.printer.ConsolePrinter;
import rush.hour.service.ui.reader.ScannerConsoleReader;
import rush.hour.service.ui.validator.InputValidator;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RushHour implements Game {

    private boolean isRunning = true;

    private GameConfig config;
    private GameState gameState;

    private GameEventPublisher publisher;
    private ErrorCounter errorCounter;

    private Scanner mapScanner;
    private ScannerMapReader scannerMapReader;

    private MapPrinter mapPrinter;
    private MapParser mapParser;
    private MapVO map;
    private MapVO playerTurn;
    private MapVO aiTurn;

    private MoveMaker moveMaker;
    private MovementValidator movementValidator;

    private ConsolePrinter consolePrinter;
    private InputValidator inputValidator;
    private Scanner consoleScanner;
    private ScannerConsoleReader scannerConsoleReader;

    private EnemyAI enemyAI;
    private Random random;

    private String username;
    private String moveChoice;

    @Override
    public void init() {

        config = GameConfig.getInstance();
        gameState = new GameState();
        gameState.setState(State.INITIALIZATION);

        publisher = new GameEventPublisher();
        errorCounter = new ErrorCounter();
        publisher.subscribe(new AuditLoggerListener());
        publisher.subscribe(new ConsoleEventPrinter());
        publisher.subscribe(errorCounter);

        publisher.publish(new GameEvent(GameEventType.DEBUG, "Reading map from file"));
        mapScanner = new Scanner(Main.class.getClassLoader().getResourceAsStream(config.getBaseMapPath()));
        scannerMapReader = new ScannerMapReader(mapScanner);

        mapPrinter = new MapPrinter();

        moveMaker = new MoveMaker();
        movementValidator = new MovementValidator();

        consolePrinter = new ConsolePrinter();
        consoleScanner = new Scanner(System.in);
        scannerConsoleReader = new ScannerConsoleReader(consoleScanner);
        inputValidator = new InputValidator(scannerConsoleReader, consolePrinter);

        random = new Random();
        enemyAI = new EnemyAI(random);

        try {
            List<String> rawMap = scannerMapReader.readMap();

            publisher.publish(new GameEvent(GameEventType.DEBUG, "Parsing map"));
            mapParser = new MapParser(config.getNumberOfRows() + config.getWallSize(),
                    config.getNumberOfColumns() + config.getWallSize());
            map = mapParser.parseMap(rawMap);

        } catch (MapReaderException e) {
            publisher.publish(new GameEvent(GameEventType.ERROR, e.getMessage()));
        }

        gameState.setState(State.BEFORE_GAME);
        consolePrinter.printMessage("Welcome to the Rush Hour game!");

        username = inputValidator.getValidInput(gameState.getState(), InputType.NAME);
    }

    @Override
    public void processInput() {
        if (gameState.getState() == State.BEFORE_GAME) {
            String input = inputValidator.getValidInput(gameState.getState(), InputType.BEFORE_GAME_CHOICE);
            int choice = Integer.parseInt(input);

            if (choice == 1) {
                gameState.setState(State.IN_GAME);
                moveChoice = null;
            } else {
                isRunning = false;
            }
        } else if (gameState.getState() == State.IN_GAME) {
            moveChoice = inputValidator.getValidInput(gameState.getState(), InputType.IN_GAME_CHOICE);
        }
    }

    @Override
    public void update() {
        if (gameState.getState() == State.IN_GAME && moveChoice != null) {
            aiTurn = null;
            playerTurn = null;
            enemyAI.nextTurn();

            if (moveChoice.equals("quit")) {
                isRunning = false;
                gameState.setState(State.END_OF_GAME);
            } else {
                try {
                    MoveVO move = moveMaker.makeMove(moveChoice, map);
                    movementValidator.validateMove(map, move);
                    playerTurn = map.moveVehicle(move);

                    do {
                        try {
                            MoveVO enemyAIMove = enemyAI.makeMove(playerTurn);
                            movementValidator.validateMove(playerTurn, enemyAIMove);
                            aiTurn = playerTurn.moveVehicle(enemyAIMove);
                        } catch (MovementValidatorException e) {

                        }
                    } while (aiTurn == null);


                } catch (MovementValidatorException | MoveMakerException e) {
                    consolePrinter.printMessage(e.getMessage());
                    moveChoice = null;
                    return;
                }


            }
            map = aiTurn;
        }
    }

    @Override
    public void render() {
        if (gameState.getState() == State.IN_GAME && moveChoice != null) {
            mapPrinter.printMap(playerTurn);
            consolePrinter.printMessage("AI's turn");
            mapPrinter.printMap(aiTurn);
            consolePrinter.printMessage("-----------------------------------------");
        } else if (gameState.getState() == State.IN_GAME && moveChoice == null) {
            mapPrinter.printMap(map);
            consolePrinter.printMessage("-----------------------------------------");
        }
    }

    @Override
    public void terminate() {
        System.out.println(errorCounter.getErrorCount());
        mapScanner.close();
        consoleScanner.close();
    }

    @Override
    public boolean getIsRunning() {
        return isRunning;
    }
}
