package gp.snake;

import gp.snake.game.Score;
import gp.snake.game.Snake;
import gp.snake.game.SnakeState;
import gp.snake.neuralNetwork.NeuralNetwork;
import io.jenetics.Chromosome;
import io.jenetics.DoubleChromosome;
import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.SinglePointCrossover;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.stat.DoubleMomentStatistics;
import io.jenetics.util.Factory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.awt.event.WindowEvent;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NeuralSnakeGame {
    private final MultiLayerNetwork neuralNetwork;
    private final int genotypeLength;
    private int generation;
    private int population;

    public NeuralSnakeGame(MultiLayerNetwork neuralNetwork, int population) {
        this.neuralNetwork = neuralNetwork;
        this.genotypeLength = (int) neuralNetwork.numParams();
        this.population = population;
    }

    private Double fitness(Genotype<DoubleGene> genotype) {
        var weights = createWeightsFromChromosome(genotype.chromosome());
        var network = NeuralNetwork.setWeights(this.neuralNetwork, weights);
        var result = runSnake(network, 5, false, false);
        if (result.getSnakeSize() > 25) {
            runSnake(network, 20, true, false);
        }
        return valueFromScore(result);
    }

    private double valueFromScore(Score result) {
        return result.getSnakeSize() * result.getFramesNumber();
//        return result.getSnakeSize();
    }

    public MultiLayerNetwork teachNeuralNetwork(double c, double m, int minutes) {
        final Factory<Genotype<DoubleGene>> gtf = Genotype.of(
            DoubleChromosome.of(-1, 1, genotypeLength)
        );

        Engine<DoubleGene, Double> engine = Engine
            .builder(this::fitness, gtf)
            .minimizing()
            .populationSize(population)
            .survivorsFraction(0.4)
//            .survivorsSelector(new EliteSelector<>(10, new TournamentSelector<DoubleGene, Double>(12)))
            .offspringSelector(new RouletteWheelSelector<>())
            .alterers(
                new SinglePointCrossover<>(c),
                new Mutator<>(m)
            )
            .executor(Executors.newSingleThreadExecutor())
            .build();

        EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

        EvolutionResult<DoubleGene, Double> weights = engine.stream()
            .limit(Limits.byExecutionTime(Duration.ofMinutes(minutes)))
            .sequential()
            .peek(statistics)
            .collect(EvolutionResult.toBestEvolutionResult());

        var chromosome = weights.bestPhenotype().genotype().chromosome();
        System.out.println("End of training, best Fitness: " + weights.bestFitness());
        var inputs = createWeightsFromChromosome(chromosome);
        return NeuralNetwork.setWeights(this.neuralNetwork, inputs);
    }

    private Map<String, INDArray> createWeightsFromChromosome(Chromosome<DoubleGene> genotype) {
        var params = neuralNetwork.paramTable();
        AtomicInteger l = new AtomicInteger();
        return params.entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    e -> {
                        var length = (int) e.getValue().shape()[0] * (int) e.getValue().shape()[1];
                        var subList = genotype.stream()
                            .skip(l.get())
                            .limit(length)
                            .mapToDouble(DoubleGene::doubleValue)
                            .toArray();
                        var result = Nd4j.create(
                            subList,
                            new int[]{(int) e.getValue().shape()[0], (int) e.getValue().shape()[1]}
                        );
                        l.addAndGet(length);
                        return result;
                    }
                )
            );
    }

    public Score runSnake(MultiLayerNetwork network, int speed, boolean visible, boolean printStates) {
        generation++;
        Snake snake = new Snake(30, visible);
        snake.run();
        if (visible) {
            snake.setTitle("Snake");
            snake.setSize(400, 400);
            snake.setVisible(true);
        }
        SnakeState state = snake.nextStep(0);
        long framesTillEnd = 100;
        long maxSize = 2;
        while (state != null && framesTillEnd > 0) {
            var input = inputFromState(state);
            state = snake.nextStep(network.predict(input)[0]);
            var score = snake.getScore();
            snake.updateScore(score.getSnakeSize());
            if (score.getSnakeSize() > maxSize) {
                if (score.getSnakeSize() > 7) {
                    printGenotypeStatistics(score);
                }
                maxSize = score.getSnakeSize();
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

    private void printGenotypeStatistics(Score score) {
        System.out.println("Generation: " + (generation / population + 1) +
            ", genotype: " + generation +
            ", snake size: " + score.getSnakeSize() +
            ", life time: " + score.getFramesNumber() +
            ", fitness: " + valueFromScore(score)
        );
    }


    private INDArray inputFromState(SnakeState state) {
        var inputArray = new double[][]{{
            state.getLeftCell(),
            state.getRightCell(),
            state.getTopCell(),
            state.getFoodAngle(),
            state.getDistance()
        }};
        return new NDArray(inputArray);
    }
}
