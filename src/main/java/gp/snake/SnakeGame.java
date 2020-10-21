package gp.snake;

import gp.snake.game.KeyboardListener;
import gp.snake.game.Score;
import gp.snake.game.Snake;
import gp.snake.game.SnakeState;
import gp.snake.jenetics.ProgramNodes;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.engine.Problem;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.Tree;
import io.jenetics.ext.util.TreeFormatter;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.stat.DoubleMomentStatistics;

import java.awt.event.WindowEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;

public class SnakeGame {

    private Double fitness(ProgramGene<Double> program) {
        var result = runSnake(program, 0, false, false);
        return Math.pow(result.getSnakeSize() * 5, 3) * result.getFramesNumber();
    }

    public void run() {
        Codec<ProgramGene<Double>, ProgramGene<Double>> CODEC = Codec.of(
            Genotype.of(ProgramChromosome.of(6, ProgramNodes.OPERATIONS, ProgramNodes.TERMINALS)),
            Genotype::getGene
        );

        Problem<ProgramGene<Double>, ProgramGene<Double>, Double> problem =
            Problem.of(this::fitness, CODEC);

        Engine<ProgramGene<Double>, Double> engine = Engine
            .builder(problem)
            .maximizing()
            .populationSize(200)
            .survivorsSelector(new RouletteWheelSelector<>())
            .alterers(
                new SingleNodeCrossover<>(0.7),
                new Mutator<>(0.1)
            )
            .executor(Executors.newSingleThreadExecutor())
            .build();

        EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

        ProgramGene<Double> tree = engine.stream()
            .limit(Limits.byExecutionTime(Duration.ofSeconds(10)))
            .peek(statistics)
            .collect(EvolutionResult.toBestResult(CODEC));

        System.out.println(statistics);
        System.out.println(TreeFormatter.TREE.format(tree));
        System.out.println(TreeFormatter.PARENTHESES.format(tree));
        runSnake(tree, 50, true, false);
//        runKeyboard(600, true, true);
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
        long framesTillEnd = 40;
        long maxSize = 2;
        while (state != null && framesTillEnd > 0) {
            var predict = algorithm.eval(state.getLeftCell(), state.getRightCell(), state.getTopCell(), state.getFoodAngle(), state.getDistance());
            var action = (int) mapRange(0, 1, 0, 4, predict);
            state = snake.nextStep(action);
            var size = snake.getScore().getSnakeSize();
            if (size > maxSize) {
                maxSize = size;
                framesTillEnd += 200;
            }
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
            framesTillEnd--;
        }
        if (visible) {
            snake.dispatchEvent(new WindowEvent(snake, WindowEvent.WINDOW_CLOSING));
        }
        return snake.getScore();
    }

    private void runKeyboard(int speed, boolean visible, boolean printStates) {
        Snake snake = new Snake(20, visible);
        snake.run();
        if (visible) {
            snake.setTitle("Snake");
            snake.setSize(500, 500);
            snake.setVisible(true);
            snake.addKeyListener(new KeyboardListener());
        }
        SnakeState state = snake.runAuto();
        while (state != null) {
            state = snake.runAuto();
            if (printStates) {
                System.out.println(state);
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static double mapRange(double a1, double a2, double b1, double b2, double s) {
        return b1 + ((s - a1) * (b2 - b1)) / (a2 - a1);
    }

    private boolean timeNotExpired(Instant start) {
        return Duration.between(start, Instant.now()).toSeconds() < 1;
    }

    private Score runSnake2(int speed, boolean visible, boolean printStates) {
        Snake snake = new Snake(20, visible);
        snake.run();
        if (visible) {
            snake.setTitle("Snake");
            snake.setSize(400, 400);
            snake.setVisible(true);
        }
        SnakeState state = snake.nextStep(0);
        Instant start = Instant.now();
        while (state != null && timeNotExpired(start)) {
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
