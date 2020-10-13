package gp.squareFunction;

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
import io.jenetics.prog.op.Op;
import io.jenetics.stat.DoubleMomentStatistics;
import io.jenetics.util.ISeq;

import java.time.Duration;
import java.util.List;

public class MathFunctionGeneticEngine {
    private List<List<Double>> data;
    private int epochs;
    private int population;
    private double crossover;
    private double mutation;
    private int depth;
    private int tournament;
    private int varNumber;
    private ISeq<Op<Double>> operations;
    private ISeq<Op<Double>> terminals;
    private boolean ifPrintStatistics;
    private boolean ifPrintOutputs;

    MathFunctionGeneticEngine(List<List<Double>> data, int epochs, int population, double crossover, double mutation,
                              int depth, int tournament, ISeq<Op<Double>> operations, ISeq<Op<Double>> terminals,
                              boolean ifPrintStatistics, boolean ifPrintOutputs) {
        this.data = data;
        this.epochs = epochs;
        this.population = population;
        this.crossover = crossover;
        this.mutation = mutation;
        this.depth = depth;
        this.tournament = tournament;
        this.varNumber = data.get(0).size() - 1;
        this.operations = operations;
        this.terminals = terminals;
        this.ifPrintStatistics = ifPrintStatistics;
        this.ifPrintOutputs = ifPrintOutputs;
    }

    void start() {
        Codec<ProgramGene<Double>, ProgramGene<Double>> CODEC = Codec.of(
            Genotype.of(ProgramChromosome.of(depth, operations, terminals)),
            Genotype::getGene
        );

        Problem<ProgramGene<Double>, ProgramGene<Double>, Double> problem =
            Problem.of(this::meanSquareError, CODEC);

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
            .limit(Limits.byFitnessThreshold(0.1))
            .limit(Limits.byExecutionTime(Duration.ofMinutes(1)))
            .peek(statistics)
            .collect(EvolutionResult.toBestResult(CODEC));

        if (ifPrintStatistics) {
            System.out.println(statistics);
        }

        if (ifPrintOutputs) {
            System.out.println(MathExpr.format(tree.toTreeNode()));
        }
    }

    private Double meanSquareError(ProgramGene<Double> program) {
        double result = 0;
        for (List<Double> datum : data) {
            result += Math.pow(valueAndExtrapolationDifference(program, datum), 2);
        }
        result += program.size();
        return result / data.size();
    }

    private double valueAndExtrapolationDifference(ProgramGene<Double> program, List<Double> datum) {
        return datum.get(varNumber) - program.eval(datum.subList(0, varNumber).toArray(new Double[0]));
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
