package rush.hour.service.map.win;

import rush.hour.model.ExitVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

public class WinDetector {
    public boolean heroCarWon(VehicleVO heroCar, ExitVO heroCarExit) {
        int exitLocation;
        int carLocation;
        if (heroCar.getOrientation() == Orientation.HORIZONTAL) {
            exitLocation = heroCarExit.getHorizontalPosition();

            // Check the rear if the exit is behind the hero car, otherwise check the front
            if (isExitBehindHeroCar(heroCar.getHorizontalPosition(), exitLocation)) {
                carLocation = heroCar.getHorizontalPosition() - heroCar.getVehicleLength() + 1;
            } else {
                carLocation = heroCar.getHorizontalPosition();
            }
        } else {
            exitLocation = heroCarExit.getVerticalPosition();

            // Check the rear if the exit is behind the hero car, otherwise check the front
            if (isExitBehindHeroCar(heroCar.getVerticalPosition(), exitLocation)) {
                carLocation = heroCar.getVerticalPosition() - heroCar.getVehicleLength() + 1;
            } else {
                carLocation = heroCar.getVerticalPosition();
            }
        }

        return isHeroCarWon(carLocation, exitLocation);
    }

    private boolean isExitBehindHeroCar(int heroCarPos, int exitPos) {
        return heroCarPos > exitPos;
    }

    private boolean isHeroCarWon(int heroCarPos, int exitPos) {
        return Math.abs(heroCarPos - exitPos) == 1;
    }
}
