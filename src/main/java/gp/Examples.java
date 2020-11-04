package gp;

import gp.snake.NeuralSnakeGame;
import gp.snake.neuralNetwork.NeuralNetwork;

public class Examples {

    public static void main(String[] args) {
//        WeaselInterpolation.run("to be or not to be.", 1000, 0.8, 0.05);
//        MathFunctionInterpolation.run("data/square_function.csv", MathNodes.OPERATIONS,
//            MathNodes.TERMINALS, 1000, 0.7, 0.1, 3, 5);
        var network = NeuralNetwork.createNeuralNetwork();
        new NeuralSnakeGame(network, 1000).run(0.5, 0.1, 5);
    }
}
