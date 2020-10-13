package gp.weasel;

import io.jenetics.CharacterChromosome;
import io.jenetics.CharacterGene;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.Phenotype;
import io.jenetics.SinglePointCrossover;
import io.jenetics.StochasticUniversalSelector;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.engine.Problem;
import io.jenetics.stat.DoubleMomentStatistics;
import io.jenetics.util.CharSeq;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.stream.IntStream;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;

public class WeaselGeneticEngine {
    private final String targetString;
    private final int population;
    private final double crossover;
    private final double mutation;
    private final boolean printSteps;
    private final boolean printStatistics;

    public WeaselGeneticEngine(String targetString, int population, double crossover, double mutation,
                               boolean printSteps, boolean printStatistics) {
        this.targetString = targetString;
        this.population = population;
        this.crossover = crossover;
        this.mutation = mutation;
        this.printSteps = printSteps;
        this.printStatistics = printStatistics;
    }

    private Problem<CharSequence, CharacterGene, Double> problem() {
        return Problem.of(
            this::getFitness,
            Codec.of(
                Genotype.of(CharacterChromosome.of(
                    CharSeq.of("a-zA-Z0-9.?,!-#$%^{}()[] "), targetString.length()
                )),
                gt -> (CharSequence) gt.chromosome()
            )
        );
    }

    private double getFitness(CharSequence word) {
        throw new NotImplementedException();
    }

    void start() {
        final Engine<CharacterGene, Double> engine = Engine.builder(problem())
            .populationSize(population)
            .survivorsSelector(new StochasticUniversalSelector<>())
            .offspringSelector(new TournamentSelector<>(5))
            .alterers(
                new Mutator<>(mutation),
                new SinglePointCrossover<>(crossover))
            .build();

        EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

        final Phenotype<CharacterGene, Double> result = engine.stream()
            .limit(Limits.byFitnessThreshold(0.99))
            .peek(word -> {
                if (printSteps) {
                    System.out.println(word.bestPhenotype());
                }
            })
            .peek(statistics)
            .collect(toBestPhenotype());

        if (printStatistics) {
            System.out.println(statistics);
            System.out.println(result);
        }
    }
}
