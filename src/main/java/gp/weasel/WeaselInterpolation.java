package gp.weasel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaselInterpolation {
    private static final Logger logger = LoggerFactory.getLogger(WeaselInterpolation.class);

    public static void run(String targetString, int population, double crossover, double mutation) {
        new WeaselGeneticEngine(targetString, population, crossover, mutation, true, true).start();
    }
}
