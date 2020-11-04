package gp.snake.jenetics;

import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

public class ProgramNodes {
    public static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
        MathOp.ADD,
        MathOp.MUL,
        Op.of("ADD3", 3, it -> it[0] + it[1] + it[2]),
        Op.of("MUL3", 3, it -> it[0] * it[1] * it[2]),
        Op.of("MUL4", 4, it -> it[0] * it[1] * it[2] * it[3]),
        Op.of("MUL5", 5, it -> it[0] * it[1] * it[2] * it[3] * it[4]),
        Op.of("RELU", 1, it -> it[0] > 0 ? it[0] : 0.0)
    );

    public static final ISeq<Op<Double>> TERMINALS = ISeq.of(
        Var.of("left", 0),
        Var.of("right", 1),
        Var.of("top", 2),
        Var.of("angle", 3),
        Var.of("distance", 4),
        EphemeralConst.of(() -> RandomRegistry.random().nextDouble())
    );
}
