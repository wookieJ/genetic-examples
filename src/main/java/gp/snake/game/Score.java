package gp.snake.game;

public class Score {
    private final long snakeSize;
    private final long framesNumber;

    public Score(long snakeSize, long framesNumber) {
        this.snakeSize = snakeSize;
        this.framesNumber = framesNumber;
    }

    @Override
    public String toString() {
        return "Score{" +
            "snakeSize=" + snakeSize +
            ", framesNumber=" + framesNumber +
            '}';
    }
}
