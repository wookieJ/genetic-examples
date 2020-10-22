package gp.snake;

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
import io.jenetics.ext.util.TreeFormatter;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.stat.DoubleMomentStatistics;

import java.awt.event.WindowEvent;
import java.time.Duration;
import java.util.concurrent.Executors;

public class SnakeGame {

    private Double fitness(ProgramGene<Double> program) {
        var result = runSnake(program, 0, false, false);
        return Math.pow(result.getSnakeSize() * 5, 3) * result.getFramesNumber();
    }

    public void run(int d, int p, double c, double m, int seconds) {
        Codec<ProgramGene<Double>, ProgramGene<Double>> CODEC = Codec.of(
            Genotype.of(ProgramChromosome.of(d, ProgramNodes.OPERATIONS, ProgramNodes.TERMINALS)),
            Genotype::getGene
        );

        Problem<ProgramGene<Double>, ProgramGene<Double>, Double> problem =
            Problem.of(this::fitness, CODEC);

        Engine<ProgramGene<Double>, Double> engine = Engine
            .builder(problem)
            .maximizing()
            .populationSize(p)
            .survivorsSelector(new RouletteWheelSelector<>())
            .alterers(
                new SingleNodeCrossover<>(c),
                new Mutator<>(m)
            )
            .executor(Executors.newSingleThreadExecutor())
            .build();

        EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

        ProgramGene<Double> tree = engine.stream()
            .limit(Limits.byExecutionTime(Duration.ofSeconds(seconds)))
            .peek(statistics)
            .collect(EvolutionResult.toBestResult(CODEC));

        System.out.println(statistics);
        System.out.println(TreeFormatter.TREE.format(tree));
        System.out.println(TreeFormatter.PARENTHESES.format(tree));
        runSnake(tree, 50, true, false);
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

    public static double mapRange(double a1, double a2, double b1, double b2, double s) {
        return b1 + ((s - a1) * (b2 - b1)) / (a2 - a1);
    }
}
