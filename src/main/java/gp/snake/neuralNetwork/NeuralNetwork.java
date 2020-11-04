package gp.snake.neuralNetwork;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Map;
import java.util.Set;

public class NeuralNetwork {
    public static MultiLayerNetwork createNeuralNetwork() {
        int inputNum = 5;
        int outputNum = 3;

        var network = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
            .trainingWorkspaceMode(WorkspaceMode.NONE)
            .list()
            .layer(new DenseLayer.Builder()
                .nIn(inputNum)
                .nOut(125)
                .activation(Activation.RELU6)
                .build())
//            .layer(new DenseLayer.Builder()
//                .nIn(5)
//                .nOut(5)
//                .activation(Activation.SIGMOID)
//                .build())
//            .layer(new DenseLayer.Builder()
//                .nIn(12)
//                .nOut(6)
//                .activation(Activation.RELU6)
//                .build())
            .layer(new DenseLayer.Builder()
                .nIn(125)
                .nOut(outputNum)
                .activation(Activation.SOFTMAX)
                .build())
            .build());
        network.init();
        return network;
    }

    public static MultiLayerNetwork setWeights(MultiLayerNetwork multiLayerNetwork, Map<String, INDArray> weights) {
        Set<String> keys = weights.keySet();
        for (String key : keys) {
            INDArray values = weights.get(key);
            multiLayerNetwork.setParam(key, values);
        }
        return multiLayerNetwork;
    }
}
