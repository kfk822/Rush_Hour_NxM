package rush.hour.model;

import java.util.Objects;

public class ExitVO {
    private final int verticalPosition;
    private final int horizontalPosition;

    private ExitVO(Builder builder){
        this.verticalPosition = builder.verticalPosition;
        this.horizontalPosition = builder.horizontalPosition;
    }

    public int getVerticalPosition() {
        return verticalPosition;
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        ExitVO exitVO = (ExitVO) o;
        return verticalPosition == exitVO.verticalPosition &&
                horizontalPosition == exitVO.horizontalPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(verticalPosition, horizontalPosition);
    }

    @Override
    public String toString(){
        return "verticalPosition(" + verticalPosition +
                "), horizontalPosition(" + horizontalPosition + ")";
    }

    public static Builder builder(){
        return new Builder();
    }


    public static final class Builder{
        private int verticalPosition;
        private int horizontalPosition;

        public Builder withVerticalPosition(int verticalPosition){
            this.verticalPosition = verticalPosition;
            return this;
        }

        public Builder withHorizontalPosition(int horizontalPosition){
            this.horizontalPosition = horizontalPosition;
            return this;
        }

        public ExitVO build(){
            return new ExitVO(this);
        }
    }
}
