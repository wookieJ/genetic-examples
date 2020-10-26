package gp.snake.neuralNetwork;

import java.util.List;

public class NetSize {
    private final int inputSize;
    private final int outputSize;
    private final List<Integer> layersOuts;

    public NetSize(int inputSize, int outputSize, List<Integer> layersOuts) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.layersOuts = layersOuts;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public List<Integer> getLayersOuts() {
        return layersOuts;
    }
}
