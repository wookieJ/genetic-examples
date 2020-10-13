package gp.squareFunction;

import io.jenetics.prog.op.Op;
import io.jenetics.util.ISeq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gp.utils.CSVLoader;

import java.util.List;

public class MathFunctionInterpolation {
    private static final Logger logger = LoggerFactory.getLogger(MathFunctionInterpolation.class);

    public static void run(String path, ISeq<Op<Double>> operations, ISeq<Op<Double>> terminals, int population,
                           double crossover, double mutation, int depth, int tournament) {
        List<List<Double>> squareFunctionData = CSVLoader.loadFromFile(path);
        if (squareFunctionData.size() < 2) {
            logger.error("Data has invalid size! Found " + squareFunctionData.size() + " but expected min 2");
            return;
        }
        new MathFunctionGeneticEngine(squareFunctionData, 100, population, crossover, mutation, depth,
                tournament, operations, terminals, true, true).start();
    }
}
