package rush.hour.service.map.win;

import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

public class WinDetector {
    public boolean heroCarWon(MapVO map, VehicleVO heroCar, ExitVO heroCarExit) {
        int exitLocation;
        int carLocation;
        if (heroCar.getOrientation() == Orientation.HORIZONTAL) {
            exitLocation = heroCarExit.getHorizontalPosition();

            if (heroCar.getVerticalPosition() != heroCarExit.getVerticalPosition()) {
                return false;
            }

            if (heroCar.getHorizontalPosition() > exitLocation) {
                carLocation = heroCar.getHorizontalPosition() - heroCar.getVehicleLength() + 1;
            } else {
                carLocation = heroCar.getHorizontalPosition();
            }
        } else {
            exitLocation = heroCarExit.getVerticalPosition();

            if (heroCar.getHorizontalPosition() != heroCarExit.getHorizontalPosition()) {
                return false;
            }

            if (heroCar.getVerticalPosition() > heroCarExit.getVerticalPosition()) {
                carLocation = heroCar.getVerticalPosition() - heroCar.getVehicleLength() + 1;
            } else {
                carLocation = heroCar.getVerticalPosition();
            }

        }

        if (Math.abs(exitLocation - carLocation) == 1) {
            return true;
        }
        return false;
    }
}
