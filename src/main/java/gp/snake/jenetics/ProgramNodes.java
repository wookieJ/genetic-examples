package gp.snake.jenetics;

import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

public class ProgramNodes {
    public static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
        Op.of("MUL2", 2, it -> it[0] * it[1]),
        MathOp.ADD,
        Op.of("gt", 2, it -> {
            if (it[0] > it[1]) {
                return 1.0;
            } else {
                return 0.0;
            }
        }),
        Op.of("POS", 1, it -> it[0] > 0 ? 1.0 : 0.0)
    );

    public static final ISeq<Op<Double>> TERMINALS = ISeq.of(
        Var.of("left", 0),
        Var.of("right", 1),
        Var.of("top", 2),
        Var.of("angle", 3),
        Var.of("distance", 4)
    );
}
