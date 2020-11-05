package gp;

import gp.snake.NeuralSnakeGame;
import gp.snake.neuralNetwork.NeuralNetwork;

public class Examples {

    public static void main(String[] args) {
//        WeaselInterpolation.run("to be or not to be.", 1000, 0.8, 0.05);
//        MathFunctionInterpolation.run("data/square_function.csv", MathNodes.OPERATIONS,
//            MathNodes.TERMINALS, 1000, 0.7, 0.1, 3, 5);
        var emptyNetwork = NeuralNetwork.createNeuralNetwork();
        var snake = new NeuralSnakeGame(emptyNetwork, 1000);
        var snakeNetwork = snake.teachNeuralNetwork(0.7, 0.1, 25);
        snake.runSnake(snakeNetwork, 100, true, true);
    }
}
