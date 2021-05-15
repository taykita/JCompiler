package JCompiler.JVM;

import JCompiler.Exceptions.JVMException.RunException;
import JCompiler.Utils.IO.Out;

import java.util.*;

public class JVM {
    public JVM(int PC) {
        for (int i = 0; i < MEM_SIZE; i++) {
            M.add(STOP);
        }
        for (int i = 0; i < MEM_SIZE / 64; i++) {
            S.add("");
        }

        this.PC = PC;
    }

    private int PC;

    public final int STOP = -1;
    public final int ADD = -2;
    public final int SUB = -3;
    public final int MULT = -4;
    public final int DIV = -5;
    public final int MOD = -6;
    public final int NEG = -7;
    public final int LOAD = -8;
    public final int SAVE = -9;
    public final int DUP = -10;
    public final int DROP = -11;
    public final int SWAP = -12;
    public final int OVER = -13;
    public final int GOTO = -14;
    public final int IFLT = -15;
    public final int IFLE = -16;
    public final int IFGT = -17;
    public final int IFGE = -18;
    public final int IFEQ = -19;
    public final int IFNE = -20;
    public final int IN = -21;
    public final int OUTLN = -22;
    public final int LN = -23;
    public final int OUTF = -24;
    public final int OUT = -25;

    private final int MEM_SIZE = 8 * 1024;
    //private final int MEM_SIZE = 64;

    private List<String> mnemo = new ArrayList<>(Arrays.asList("",
            "STOP", "ADD", "SUB", "MULT",
            "DIV", "MOD", "NEG", "LOAD",
            "SAVE", "DUP", "DROP", "SWAP",
            "OVER", "GOTO", "IFLT", "IFLE",
            "IFGT", "IFGE", "IFEQ", "IFNE",
            "IN", "OUTLN", "LN"));

    private int SPSTR;
    private int SP;
    private int count;

    List<Integer> M = new ArrayList<>();
    List<String> S = new ArrayList<>();

    public void addStr(String str) {
        S.set(SPSTR, str);
        SPSTR++;
    }

    public void Run() {
        PC = 0;
        SP = MEM_SIZE;
        SPSTR = 0;
        count = 0;
        while (true) {
            count++;
            int cmd = M.get(PC);
            PC++;
            if (cmd >= 0) {
                SP--;
                M.set(SP, cmd);
            } else if (cmd == ADD) {
                SP++;
                M.set(SP, M.get(SP) + M.get(SP - 1));
            } else if (cmd == SUB) {
                SP++;
                M.set(SP, M.get(SP) - M.get(SP - 1));
            } else if (cmd == MULT) {
                SP++;
                M.set(SP, M.get(SP) * M.get(SP - 1));
            } else if (cmd == DIV) {
                SP++;
                M.set(SP, M.get(SP) / M.get(SP - 1));
            } else if (cmd == MOD) {
                SP++;
                M.set(SP, M.get(SP) % M.get(SP - 1));
            } else if (cmd == NEG) {
                M.set(SP, -M.get(SP));
            } else if (cmd == LOAD) {
                M.set(SP, M.get(M.get(SP)));
            } else if (cmd == SAVE) {
                M.set(M.get(SP + 1), M.get(SP));
                SP += 2;
            } else if (cmd == DUP) {
                M.set(SP - 1, M.get(SP));
                SP--;
            } else if (cmd == DROP) {
                SP++;
            } else if (cmd == SWAP) {
                int temp = M.get(SP);
                M.set(SP, M.get(SP + 1));
                M.set(SP + 1, temp);
            } else if (cmd == OVER) {
                SP--;
                M.set(SP, M.get(SP + 2));
            } else if (cmd == GOTO) {
                PC = M.get(SP);
                SP++;
            } else if (cmd == IFEQ) {
                if (M.get(SP + 2).equals(M.get(SP + 1))) {
                    PC = M.get(SP);
                }
                SP += 3;
            } else if (cmd == IFNE) {
                if (!(M.get(SP + 2).equals(M.get(SP + 1)))) {
                    PC = M.get(SP);
                }
                SP += 3;
            } else if (cmd == IFLT) {
                if (M.get(SP + 2) < M.get(SP + 1)) {
                    PC = M.get(SP);
                }
                SP += 3;
            } else if (cmd == IFLE) {
                if (M.get(SP + 2) <= M.get(SP + 1)) {
                    PC = M.get(SP);
                }
                SP += 3;
            } else if (cmd == IFGT) {
                if (M.get(SP + 2) > M.get(SP + 1)) {
                    PC = M.get(SP);
                }
                SP += 3;
            } else if (cmd == IFGE) {
                if (M.get(SP + 2) >= M.get(SP + 1)) {
                    PC = M.get(SP);
                }
                SP += 3;
            } else if (cmd == IN) {
                SP--;
                try {
                    Scanner read = new Scanner(System.in);
                    new Out().out("?");
                    M.set(SP, read.nextInt());
                } catch (InputMismatchException e) {
                    throw new RunException("Неправильный ввод");
                }
            } else if (cmd == OUTLN) {
                new Out().outLn(M.get(SP));
                SP++;
            } else if (cmd == OUT) {
                new Out().out(M.get(SP));
                SP++;
            } else if (cmd == OUTF) {
                try {
                    System.out.printf(S.get(SPSTR), M.get(SP));
                } catch (RuntimeException e) {
                    throw new RunException("Неверный формат");
                }
                SPSTR--;
                SP++;
            } else if (cmd == LN) {
                new Out().outLn("");
            } else if (cmd == STOP) {
                break;
            } else {
                throw new RunException("Недопустимая команда");
            }
        }
        System.out.println("\nКоличество тактов " + count);
        if (SP < MEM_SIZE) {
            new Out().outLn("Код возврата " + M.get(SP));
        }
    }

    public void printCode(int PC) {
        for (int i = 0; i < PC; i++) {
            System.out.print(i + ") ");
            if (M.get(i) >= 0) {
                System.out.println(M.get(i));
            } else {
                System.out.println(mnemo.get(-M.get(i)));
            }
        }
    }

    public List<Integer> getM() {
        return M;
    }

}
