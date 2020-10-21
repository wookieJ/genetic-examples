package gp.snake;

import gp.snake.game.Score;
import gp.snake.game.Snake;
import gp.snake.game.SnakeState;
import io.jenetics.prog.ProgramGene;

import java.awt.event.WindowEvent;

public class SnakeGame {
    public static void run() {
        System.out.println("END! Score: " + runSnake(null, false, true));
    }

    private static Score runSnake(ProgramGene<SnakeState> algorithm, boolean visible, boolean printStates) {
        Snake snake = new Snake(30, visible);
        snake.run();
        if (visible) {
            snake.setTitle("Snake");
            snake.setSize(500, 500);
            snake.setVisible(true);
        }
        SnakeState state = snake.nextStep(0);
        while (state != null) {
            var action = algorithm.eval(state);
            state = snake.nextStep(0);
            if (printStates) {
                System.out.println(state);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (visible) {
            snake.dispatchEvent(new WindowEvent(snake, WindowEvent.WINDOW_CLOSING));
        }
        return snake.getScore();
    }
}
