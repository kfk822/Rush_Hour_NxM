package rush.hour.model;

import java.util.Objects;

public class MoveVO {
    private final VehicleVO vehicle;
    private final int targetRow;
    private final int targetColumn;

    private MoveVO(Builder builder) {
        this.vehicle = builder.vehicle;
        this.targetRow = builder.targetRow;
        this.targetColumn = builder.targetColumn;
    }

    public VehicleVO getVehicle() {
        return vehicle;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetColumn() {
        return targetColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoveVO moveVO = (MoveVO) o;
        return targetRow == moveVO.targetRow &&
                targetColumn == moveVO.targetColumn &&
                Objects.equals(vehicle, moveVO.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicle, targetRow, targetColumn);
    }

    public Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private VehicleVO vehicle;
        private int targetRow;
        private int targetColumn;

        public Builder withVehicle(VehicleVO vehicle) {
            this.vehicle = vehicle;
            return this;
        }

        public Builder withTargetRow(int targetRow) {
            this.targetRow = targetRow;
            return this;
        }

        public Builder withTargetColumn(int targetColumn) {
            this.targetColumn = targetColumn;
            return this;
        }

        public MoveVO build() {
            if (vehicle == null) {
                throw new IllegalStateException("Vehicle can't be null!");
            }
            if (targetColumn < 0 || targetRow < 0) {
                throw new IllegalStateException("TargetRow and targetColumn must be at least 0");
            }

            return new MoveVO(this);
        }
    }
}
