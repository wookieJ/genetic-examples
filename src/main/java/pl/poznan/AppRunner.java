package pl.poznan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.poznan.gp.MathFunctionGeneticEngine;
import pl.poznan.utils.CSVLoader;

import java.util.List;

@SpringBootApplication
public class AppRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    public static void main(String[] args) {
        List<List<Double>> squareFunctionData = CSVLoader.loadFromFile("data/square_function.csv");
        if (squareFunctionData.size() != 2) {
            logger.error("Data has invalid size! (Found " + squareFunctionData.size() + ") but expected 2");
        }
        new MathFunctionGeneticEngine(squareFunctionData.get(0), squareFunctionData.get(1), 100, 30, 0.7, 0.05, 3, 5, true, true)
                .start();
    }
}