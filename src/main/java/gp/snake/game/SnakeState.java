package gp.snake.game;

public class SnakeState {
    private final double leftCell;
    private final double rightCell;
    private final double topCell;
    private final double foodAngle;
    private final double distance;

    public double getLeftCell() {
        return leftCell;
    }

    public double getRightCell() {
        return rightCell;
    }

    public double getTopCell() {
        return topCell;
    }

    public double getFoodAngle() {
        return foodAngle;
    }

    public double getDistance() {
        return distance;
    }

    public SnakeState(double leftCell, double rightCell, double topCell, double foodAngle, double distance) {
        this.leftCell = leftCell;
        this.rightCell = rightCell;
        this.topCell = topCell;
        this.foodAngle = foodAngle;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "[" + leftCell + ", " + rightCell + ", " + topCell + ", " + foodAngle + ". " + distance + ']';
    }
}
