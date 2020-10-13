package gp.squareFunction;

import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

public class MathNodes {

    public static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
            MathOp.ADD,
            MathOp.SUB,
            MathOp.MUL,
            MathOp.SQR
    );

    public static final ISeq<Op<Double>> TERMINALS = ISeq.of(
            Var.of("x", 0),
            EphemeralConst.of(() -> (double) RandomRegistry.random().nextInt(10))
    );
}
