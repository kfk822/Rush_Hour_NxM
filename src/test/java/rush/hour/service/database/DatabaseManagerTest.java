package rush.hour.service.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rush.hour.service.exceptions.DatabaseManagerException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseManagerTest {

    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        String testUrl = "jdbc:h2:mem:testdb_" + UUID.randomUUID();
        databaseManager = new DatabaseManager(testUrl);
    }

    @AfterEach
    void tearDown() {
        databaseManager.close();
    }

    @Test
    void testConstructorShouldThrowExceptionWhenUrlIsInvalid() {
        assertThrows(DatabaseManagerException.class,
                () -> new DatabaseManager("jdbc:invalid:url"));
    }

    @Test
    void testPlayerExistsShouldReturnFalseWhenPlayerIsNotInDatabase() {
        assertFalse(databaseManager.playerExists("NonExistentPlayer"));
    }

    @Test
    void testAddPlayerShouldInsertPlayerWithZeroScore() {
        databaseManager.addPlayer("Alice");

        assertTrue(databaseManager.playerExists("Alice"));
        assertEquals("Alice - 0\n", databaseManager.getLeaderboard());
    }

    @Test
    void testAddPlayerShouldThrowExceptionWhenPlayerAlreadyExists() {
        databaseManager.addPlayer("Alice");

        assertThrows(DatabaseManagerException.class,
                () -> databaseManager.addPlayer("Alice"));
    }

    @Test
    void testIncreaseScoreShouldIncrementPlayerScore() {
        databaseManager.addPlayer("Bob");
        databaseManager.increaseScore("Bob");
        databaseManager.increaseScore("Bob");

        assertEquals("Bob - 2\n", databaseManager.getLeaderboard());
    }

    @Test
    void testGetLeaderboardShouldReturnEmptyStringWhenNoPlayersExist() {
        assertEquals("", databaseManager.getLeaderboard());
    }

    @Test
    void testGetLeaderboardShouldReturnPlayersOrderedByScoreDescending() {
        databaseManager.addPlayer("Alice");
        databaseManager.addPlayer("Bob");
        databaseManager.addPlayer("Charlie");

        databaseManager.increaseScore("Bob");
        databaseManager.increaseScore("Bob");
        databaseManager.increaseScore("Charlie");

        String expected = "Bob - 2\nCharlie - 1\nAlice - 0\n";
        assertEquals(expected, databaseManager.getLeaderboard());
    }

    @Test
    void testMethodsShouldThrowExceptionWhenConnectionIsClosed() {
        databaseManager.close();

        assertThrows(DatabaseManagerException.class,
                () -> databaseManager.playerExists("Alice"));
        assertThrows(DatabaseManagerException.class,
                () -> databaseManager.increaseScore("Alice"));
        assertThrows(DatabaseManagerException.class,
                () -> databaseManager.getLeaderboard());
    }
}