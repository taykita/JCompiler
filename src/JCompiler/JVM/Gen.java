package JCompiler.JVM;

import JCompiler.Parser.Items.Const;
import JCompiler.Parser.Items.Var;
import JCompiler.Scanner.Lex;
import java.util.List;

public class Gen {
    public Gen(int PC, List<Integer> M) {
        this.PC = PC;
        this.M = M;
    }

    private final List<Integer> M;
    private int PC;


    public void gen(int cmd) {
        M.set(PC, cmd);
        PC++;
    }

    public void genConst(int c) {
        gen(Math.abs(c));
        if (c < 0) {
            gen(-7);
        }
    }

    public void genAddr(Var v) {
        gen(v.getAddr());
        v.setAddr(PC + 1);
    }

    public void genComp(Lex op) {
        gen(0);
        if (op == Lex.EQ) {
            gen(-20);
        } else if (op == Lex.NE) {
            gen(-19);
        } else if (op == Lex.GE) {
            gen(-15);
        } else if (op == Lex.GT) {
            gen(-16);
        } else if (op == Lex.LE) {
            gen(-17);
        } else if (op == Lex.LT) {
            gen(-18);
        }
    }

    public void fixup(int A, int PC) {
        while (A > 0) {
            int temp = M.get(A - 2);
            M.set(A - 2, PC);
            A = temp;
        }
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getPC() {
        return PC;
    }
}
