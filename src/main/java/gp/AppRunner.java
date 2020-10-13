package gp;

import gp.squareFunction.MathFunctionInterpolation;
import gp.squareFunction.MathNodes;
import gp.weasel.WeaselInterpolation;

public class AppRunner {

    public static void main(String[] args) {
        MathFunctionInterpolation.run("data/square_function.csv", MathNodes.OPERATIONS,
            MathNodes.TERMINALS, 1000, 0.7, 0.1, 3, 5);
        WeaselInterpolation.run("to be or not to be.", 1000, 0.8, 0.05);
    }
}
