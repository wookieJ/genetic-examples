package gp.snake.game;

public class SnakeState {
    private final double leftCell;
    private final double rightCell;
    private final double topCell;
    private final double foodAngle;

    public SnakeState(double leftCell, double rightCell, double topCell, double foodAngle) {
        this.leftCell = leftCell;
        this.rightCell = rightCell;
        this.topCell = topCell;
        this.foodAngle = foodAngle;
    }

    @Override
    public String toString() {
        return "[" + leftCell + ", " + rightCell + ", " + topCell + ", " + foodAngle + ']';
    }
}
