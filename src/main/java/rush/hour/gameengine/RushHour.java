package rush.hour.gameengine;

import rush.hour.Main;
import rush.hour.config.GameConfig;
import rush.hour.event.GameEvent;
import rush.hour.event.GameEventPublisher;
import rush.hour.event.GameEventType;
import rush.hour.event.listener.AuditLoggerListener;
import rush.hour.event.listener.ErrorCounter;
import rush.hour.model.*;
import rush.hour.service.database.DatabaseManager;
import rush.hour.service.enemy.EnemyAI;
import rush.hour.service.exceptions.*;
import rush.hour.service.map.json.loader.JSONMapLoader;
import rush.hour.service.map.json.saver.JSONMapSaver;
import rush.hour.service.map.parser.MapParser;
import rush.hour.service.map.printer.MapPrinter;
import rush.hour.service.map.reader.ScannerMapReader;
import rush.hour.service.map.validator.MovementValidator;
import rush.hour.service.map.win.WinDetector;
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

    private WinDetector winDetector;

    private JSONMapLoader jsonMapLoader;
    private JSONMapSaver jsonMapSaver;

    private DatabaseManager databaseManager;

    private String username;
    private String moveChoice;

    @Override
    public void init() {
        initPublisher();
        initGameCore();
        initIO();
        initEnemy();

        initDatabase();
        if (!isRunning) {
            return;
        }

        initMap();
        if (!isRunning) {
            return;
        }

        welcome();
        getUserName();
    }

    @Override
    public void processInput() {
        switch (gameState.getState()) {
            case BEFORE_GAME -> getBeforeGameInput();
            case FIRST_TURN -> {
                gameState.setState(State.IN_GAME);
                getInGameInput();
            }
            case IN_GAME -> getInGameInput();
            default -> {
            }
        }
    }

    @Override
    public void update() {
        if (gameState.getState() == State.IN_GAME) {
            if (moveChoice.equals("1")) {
                saveGame();
            } else if (moveChoice.equals("2")) {
                aiTurn = null;
                playerTurn = null;
                leaveGame();
            } else {
                nextRound();
            }
        }
    }

    @Override
    public void render() {
        switch (gameState.getState()) {
            case FIRST_TURN -> printStarterMap();
            case IN_GAME -> {
                if (moveChoice != null) {
                    printTurns();
                }
            }
            case END_OF_GAME -> {
                printTurns();
                consolePrinter.printMessage("The game has ended");
            }
            default -> {
            }
        }
    }

    @Override
    public void terminate() {
        log(GameEventType.DEBUG, "Terminating.");
        log(GameEventType.DEBUG, "Error count: " + errorCounter.getErrorCount());
        mapScanner.close();
        consoleScanner.close();
        if (databaseManager != null) {
            databaseManager.close();
        }
    }

    @Override
    public boolean getIsRunning() {
        return isRunning;
    }

    private void log(GameEventType type, String message) {
        publisher.publish(new GameEvent(type, message));
    }

    private void initPublisher() {
        publisher = new GameEventPublisher();
        errorCounter = new ErrorCounter();
        publisher.subscribe(new AuditLoggerListener());
        publisher.subscribe(errorCounter);
    }

    private void initGameCore() {
        config = GameConfig.getInstance();
        gameState = new GameState();
        gameState.setState(State.INITIALIZATION);
        log(GameEventType.INFO, "New game state: " + gameState.getState().toString());
    }

    private void initDatabase() {
        try {
            log(GameEventType.DEBUG, "Connecting to the database.");
            databaseManager = new DatabaseManager();
        } catch (DatabaseManagerException e) {
            log(GameEventType.ERROR, e.getMessage());
            isRunning = false;
        }
    }

    private void initIO() {
        mapPrinter = new MapPrinter();
        consolePrinter = new ConsolePrinter();
        consoleScanner = new Scanner(System.in);
        scannerConsoleReader = new ScannerConsoleReader(consoleScanner);
        inputValidator = new InputValidator(scannerConsoleReader, consolePrinter);
        jsonMapLoader = new JSONMapLoader();
        jsonMapSaver = new JSONMapSaver();
        mapScanner = new Scanner(Main.class.getClassLoader().getResourceAsStream(config.getBaseMapPath()));
        scannerMapReader = new ScannerMapReader(mapScanner);
    }

    private void initEnemy() {
        random = new Random();
        enemyAI = new EnemyAI(random);
    }

    private void initMap() {
        winDetector = new WinDetector();
        moveMaker = new MoveMaker();
        movementValidator = new MovementValidator();
        try {
            log(GameEventType.DEBUG, "Reading default map.");
            List<String> rawMap = scannerMapReader.readMap();

            log(GameEventType.DEBUG, "Parsing default map.");
            mapParser = new MapParser(config.getNumberOfRows() + config.getWallSize(),
                    config.getNumberOfColumns() + config.getWallSize());
            map = mapParser.parseMap(rawMap);
            log(GameEventType.INFO, "Default map loaded.");

        } catch (MapReaderException | IllegalStateException | IllegalArgumentException e) {
            log(GameEventType.ERROR, e.getMessage());
            isRunning = false;
        }
    }

    private void welcome() {
        gameState.setState(State.BEFORE_GAME);
        log(GameEventType.INFO, "New game state: " + gameState.getState().toString());
        consolePrinter.printMessage("Welcome to the Rush Hour game!");
    }

    private void getUserName() {
        username = inputValidator.getValidInput(gameState.getState(), InputType.NAME);
        log(GameEventType.INFO, "Player " + username + " joined the game.");
        try {
            if (!databaseManager.playerExists(username)) {

                databaseManager.addPlayer(username);
                log(GameEventType.DEBUG, "Player " + username + " added to the database.");
            }
        } catch (DatabaseManagerException e) {
            log(GameEventType.ERROR, e.getMessage());
            isRunning = false;
        }
    }

    private void getBeforeGameInput() {
        String input = inputValidator.getValidInput(gameState.getState(), InputType.BEFORE_GAME_CHOICE);
        int choice = Integer.parseInt(input);

        if (choice == 1) {
            startGame();
        } else if (choice == 2) {
            loadGame();
        } else if (choice == 3) {
            printLeaderboard();

        } else {
            leaveGame();
        }
    }

    private void getInGameInput() {
        moveChoice = inputValidator.getValidInput(gameState.getState(), InputType.IN_GAME_CHOICE);
    }

    private void startGame() {
        gameState.setState(State.FIRST_TURN);
        log(GameEventType.INFO, "New game state: " + gameState.getState().toString());
    }

    private void loadGame() {
        try {
            String filename = inputValidator.getValidInput(gameState.getState(), InputType.MAP_FILE_NAME);
            map = jsonMapLoader.loadMap(filename);
            log(GameEventType.INFO, "Map loaded from JSON file: " + filename + ".json");
            startGame();
        } catch (JSONMapLoaderException e) {
            consolePrinter.printMessage(e.getMessage());
            log(GameEventType.WARN, e.getMessage());
        }
    }

    private void printLeaderboard() {
        try {
            log(GameEventType.DEBUG, "Printing the leaderboard.");
            String leaderboard = databaseManager.getLeaderboard();
            consolePrinter.printMessage("Leaderboard:");
            consolePrinter.printMessage(leaderboard);
        } catch (DatabaseManagerException e) {
            log(GameEventType.ERROR, e.getMessage());
            isRunning = false;
        }
    }

    private void nextRound() {
        try {
            movePlayer();

            if (winDetector.heroCarWon(playerTurn.getHeroCarPlayer(), playerTurn.getExitPlayer())) {
                consolePrinter.printMessage("Player '" + username + "' won.");
                log(GameEventType.INFO, "Player '" + username + "' won.");
                increaseScore(username);
                leaveGame();
                return;
            }

            moveAI();

            if (winDetector.heroCarWon(aiTurn.getHeroCarAI(), aiTurn.getExitAI())) {
                consolePrinter.printMessage("AI won");
                log(GameEventType.INFO, "AI won");

                leaveGame();
            }
        } catch (MovementValidatorException | MoveMakerException e) {
            consolePrinter.printMessage(e.getMessage());
            log(GameEventType.WARN, e.getMessage());
            moveChoice = null;
        }
    }

    private void increaseScore(String username) {
        try {
            databaseManager.increaseScore(username);
            log(GameEventType.DEBUG, "Player " + username + "'s score increased");
        } catch (DatabaseManagerException e) {
            log(GameEventType.ERROR, e.getMessage());
        }
    }

    private void movePlayer() {
        playerTurn = null;
        log(GameEventType.DEBUG, "Player " + username + "'s turn.");
        MoveVO move = moveMaker.makeMove(moveChoice, map);
        movementValidator.validateMove(map, move);
        playerTurn = map.moveVehicle(move);
        log(GameEventType.DEBUG, "Player " + username + " moved: " + moveChoice);
    }

    private void moveAI() {
        aiTurn = null;
        enemyAI.nextTurn();
        int attempt = 1;
        do {
            try {
                MoveVO enemyAIMove = enemyAI.makeMove(playerTurn);
                movementValidator.validateMove(playerTurn, enemyAIMove);
                aiTurn = playerTurn.moveVehicle(enemyAIMove);
                log(GameEventType.DEBUG, "AI moved on attempt " + attempt);
            } catch (MovementValidatorException e) {
                attempt++;
            }
        } while (aiTurn == null);
        map = aiTurn;
    }

    private void saveGame() {
        String filename = inputValidator.getValidInput(gameState.getState(), InputType.MAP_FILE_NAME);
        try {
            log(GameEventType.DEBUG, "Saving map.");
            jsonMapSaver.saveMap(map, filename);
            log(GameEventType.INFO, "Map saved to JSON file: " + filename + ".json");
            aiTurn = null;
            playerTurn = null;
            leaveGame();
        } catch (JSONMapSaverException e) {
            log(GameEventType.ERROR, e.getMessage());
            isRunning = false;
        }
    }

    private void leaveGame() {
        log(GameEventType.DEBUG, "Leaving the game.");
        isRunning = false;
        gameState.setState(State.END_OF_GAME);
        log(GameEventType.INFO, "New game state: " + gameState.getState().toString());
    }

    private void printTurns() {
        if (playerTurn != null) {
            log(GameEventType.DEBUG, "Printing player " + username + "'s turn.");
            mapPrinter.printMap(playerTurn);
        }
        if (aiTurn != null) {
            log(GameEventType.DEBUG, "Printing AI's turn.");
            consolePrinter.printMessage("AI's turn");
            mapPrinter.printMap(aiTurn);
        }
        if (playerTurn != null || aiTurn != null) {
            consolePrinter.printMessage("-----------------------------------------");
        }
    }

    private void printStarterMap() {
        log(GameEventType.DEBUG, "Printing map.");
        mapPrinter.printMap(map);
        consolePrinter.printMessage("-----------------------------------------");
    }
}
