package pl.poznan.gp;

import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.engine.Problem;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.MathExpr;
import io.jenetics.stat.DoubleMomentStatistics;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MathFunctionGeneticEngine {
    List<Double> arguments;
    List<Double> values;
    private int epochs;
    private int population;
    private double crossover;
    private double mutation;
    private int depth;
    private int tournament;
    private boolean ifPrintStatistics;
    private boolean ifPrintOutputs;

    public MathFunctionGeneticEngine(List<Double> arguments, List<Double> values, int epochs, int population, double crossover, double mutation, int depth, int tournament,
                                     boolean ifPrintStatistics, boolean ifPrintOutputs) {
        this.arguments = arguments;
        this.values = values;
        this.epochs = epochs;
        this.population = population;
        this.crossover = crossover;
        this.mutation = mutation;
        this.depth = depth;
        this.tournament = tournament;
        this.ifPrintStatistics = ifPrintStatistics;
        this.ifPrintOutputs = ifPrintOutputs;
    }

    public void start() {
        Codec<ProgramGene<Double>, ProgramGene<Double>> CODEC = Codec.of(
                Genotype.of(
                        ProgramChromosome.of(
                                depth,
                                Nodes.OPERATIONS,
                                Nodes.TERMINALS
                        )),
                Genotype::getGene
        );

        Problem<ProgramGene<Double>, ProgramGene<Double>, Double> problem =
                Problem.of(
                        fitness(),
                        CODEC
                );

        Engine<ProgramGene<Double>, Double> engine = Engine
                .builder(problem)
                .minimizing()
                .populationSize(population)
                .survivorsSelector(new TournamentSelector<>(tournament))
                .alterers(
                        new SingleNodeCrossover<>(crossover),
                        new Mutator<>(mutation)
                )
                .build();

        EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

        ProgramGene<Double> tree = engine.stream()
                .limit(Limits.byFitnessThreshold(0.05))
                .limit(Limits.byExecutionTime(Duration.ofMinutes(1)))
                .peek(statistics)
                .collect(EvolutionResult.toBestResult(CODEC));

        if (ifPrintStatistics) {
            System.out.println(statistics);
        }

        if (ifPrintOutputs) {
            System.out.println(new MathExpr(tree.toTreeNode()));
        }
    }

    private Function<ProgramGene<Double>, Double> fitness() {
        return (ProgramGene<Double> program) -> {
            double result = 0;
            for (int i = 0; i < arguments.size(); i++) {
                result += Math.pow(values.get(i) - program.eval(arguments.get(i)), 2);
            }
            return result / arguments.size();
        };
    }

    @Override
    public String toString() {
        return "ProcessTree{" +
                "epochs=" + epochs +
                ", population=" + population +
                ", crossover=" + crossover +
                ", mutation=" + mutation +
                ", depth=" + depth +
                ", tournament=" + tournament +
                '}';
    }
}
