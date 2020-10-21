package gp.snake;

import gp.snake.game.Score;
import gp.snake.game.Snake;
import gp.snake.game.SnakeState;
import io.jenetics.prog.ProgramGene;

import java.awt.event.WindowEvent;

public class SnakeGame {

    public void run() {
        runSnake(null, 50, true, true);
    }

    private Score runSnake(ProgramGene<Double> algorithm, int speed, boolean visible, boolean printStates) {
        Snake snake = new Snake(20, visible);
        snake.run();
        if (visible) {
            snake.setTitle("Snake");
            snake.setSize(400, 400);
            snake.setVisible(true);
        }
        SnakeState state = snake.nextStep(0);
        while (state != null) {
            var action = (int) (Math.random() * 4);
            state = snake.nextStep(action);
            if (printStates) {
                System.out.println(state);
            }
            if (visible) {
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (visible) {
            snake.dispatchEvent(new WindowEvent(snake, WindowEvent.WINDOW_CLOSING));
        }
        return snake.getScore();
    }
}
