package rush.hour.model;

import java.util.Objects;

public class VehicleVO {
    private final int vehicleLength;
    private final Orientation orientation;
    private final int verticalPosition;
    private final int horizontalPosition;

    private VehicleVO(Builder builder){
        this.vehicleLength = builder.vehicleLength;
        this.orientation = builder.orientation;
        this.horizontalPosition = builder.horizontalPosition;
        this.verticalPosition = builder.verticalPosition;
    }

    public int getVehicleLength(){
        return vehicleLength;
    }

    public int getVerticalPosition() {
        return verticalPosition;
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        VehicleVO vechileVO = (VehicleVO) o;

        return vehicleLength == vechileVO.vehicleLength &&
                verticalPosition == vechileVO.verticalPosition &&
                horizontalPosition == vechileVO.horizontalPosition &&
                orientation == vechileVO.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleLength, orientation, verticalPosition, horizontalPosition);
    }

    public VehicleVO withValue(int verticalPosition, int horizontalPosition){
        return builder()
                .withVehicleLength(vehicleLength)
                .withOrientation(orientation)
                .withVerticalPosition(verticalPosition)
                .withHorizontalPosition(horizontalPosition)
                .build();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder{
        private int vehicleLength;
        private Orientation orientation;
        private int verticalPosition;
        private int horizontalPosition;

        public Builder withVehicleLength(int length){
            this.vehicleLength = length;
            return this;
        }

        public Builder withOrientation(Orientation orientation){
            this.orientation = orientation;
            return this;
        }

        public Builder withVerticalPosition(int verticalPosition){
            this.verticalPosition = verticalPosition;
            return this;
        }

        public Builder withHorizontalPosition(int horizontalPosition){
            this.horizontalPosition = horizontalPosition;
            return this;
        }

        public VehicleVO build(){
            if(vehicleLength < 2 || vehicleLength > 3) {
                throw new IllegalStateException("The length must be 2 or 3!");
            }
            if(orientation == null){
                throw new IllegalStateException("Orientation is null!");
            }

            return new VehicleVO(this);
        }
    }
}
