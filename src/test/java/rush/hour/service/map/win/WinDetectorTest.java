package rush.hour.service.map.win;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WinDetectorTest {

    private WinDetector winDetector;

    @BeforeEach
    void setUp() {
        winDetector = new WinDetector();
    }

    @Test
    @DisplayName("Should return true when horizontal car head is right next to right exit")
    void testHeroCarWonShouldReturnTrueWhenHorizontalCarReachesRightExit() {
        VehicleVO heroCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(5)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        ExitVO exit = new ExitVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(6)
                .build();

        assertTrue(winDetector.heroCarWon(heroCar, exit));
    }

    @Test
    @DisplayName("Should return true when horizontal car tail is right next to left exit")
    void testHeroCarWonShouldReturnTrueWhenHorizontalCarReachesLeftExit() {
        VehicleVO heroCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        ExitVO exit = new ExitVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(0)
                .build();

        assertTrue(winDetector.heroCarWon(heroCar, exit));
    }

    @Test
    @DisplayName("Should return false when horizontal car is not adjacent to right exit")
    void testHeroCarWonShouldReturnFalseWhenHorizontalCarIsFarFromRightExit() {
        VehicleVO heroCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(3)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        ExitVO exit = new ExitVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(6)
                .build();

        assertFalse(winDetector.heroCarWon(heroCar, exit));
    }

    @Test
    @DisplayName("Should return false when horizontal car is not adjacent to left exit")
    void testHeroCarWonShouldReturnFalseWhenHorizontalCarIsFarFromLeftExit() {
        VehicleVO heroCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(4)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        ExitVO exit = new ExitVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(0)
                .build();

        assertFalse(winDetector.heroCarWon(heroCar, exit));
    }

    @Test
    @DisplayName("Should return true when vertical car head is right next to bottom exit")
    void testHeroCarWonShouldReturnTrueWhenVerticalCarReachesBottomExit() {
        VehicleVO heroCar = new VehicleVO.Builder()
                .withVerticalPosition(4)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        ExitVO exit = new ExitVO.Builder()
                .withVerticalPosition(5)
                .withHorizontalPosition(2)
                .build();

        assertTrue(winDetector.heroCarWon(heroCar, exit));
    }

    @Test
    @DisplayName("Should return true when vertical car tail is right next to top exit")
    void testHeroCarWonShouldReturnTrueWhenVerticalCarReachesTopExit() {
        VehicleVO heroCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        ExitVO exit = new ExitVO.Builder()
                .withVerticalPosition(0)
                .withHorizontalPosition(2)
                .build();

        assertTrue(winDetector.heroCarWon(heroCar, exit));
    }

    @Test
    @DisplayName("Should return false when vertical car is not adjacent to bottom exit")
    void testHeroCarWonShouldReturnFalseWhenVerticalCarIsFarFromBottomExit() {
        VehicleVO heroCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        ExitVO exit = new ExitVO.Builder()
                .withVerticalPosition(5)
                .withHorizontalPosition(2)
                .build();

        assertFalse(winDetector.heroCarWon(heroCar, exit));
    }

    @Test
    @DisplayName("Should return false when vertical car is not adjacent to top exit")
    void testHeroCarWonShouldReturnFalseWhenVerticalCarIsFarFromTopExit() {
        VehicleVO heroCar = new VehicleVO.Builder()
                .withVerticalPosition(4)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        ExitVO exit = new ExitVO.Builder()
                .withVerticalPosition(0)
                .withHorizontalPosition(2)
                .build();

        assertFalse(winDetector.heroCarWon(heroCar, exit));
    }
}