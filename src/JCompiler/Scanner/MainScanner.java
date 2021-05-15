package JCompiler.Scanner;

import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Scanner.LiteralScanner.LiteralScanner;
import JCompiler.Text;

import java.util.ArrayList;
import java.util.List;


public class MainScanner {
    public MainScanner(Text text) {
        this.t = text;
        this.nameScanner = new NameScanner(this.t, this);
        this.literalScanner = new LiteralScanner(this.t, this);
        this.separatorScanner = new SeparatorScanner(this.t, this);
        this.operatorScanner = new OperatorScanner(this.t);

        this.commentHandler = new CommentHandler(this.t);

        initScanners();
        t.setNextCh();
    }

    private final Text t;

    private Lex lex = Lex.NONE;

    private String name;
    private int intNum;
    private String strLit;

    private final NameScanner nameScanner;
    private final LiteralScanner literalScanner;
    private final SeparatorScanner separatorScanner;
    private final OperatorScanner operatorScanner;

    private final CommentHandler commentHandler;

    List<Scanner> scanners = new ArrayList<>();

    private void initScanners() {
        scanners.add(nameScanner);
        scanners.add(literalScanner);
        scanners.add(separatorScanner);
        scanners.add(operatorScanner);
    }

    private void skipSpace() {
        while (!t.chIsChar()) {
            t.setNextCh();
        }
    }

    private void checkEOT() {
        if (lex == Lex.NONE) {
            if (t.chIsEOT()) {
                lex = Lex.EOT;
            } else {
                throw new LexException(t, "Неизвестный символ");
            }
        }
    }

    private void checkComment() {
        if (lex == Lex.DIV) {
            if (t.getCh() == '/') {
                t.setNextCh();
                commentHandler.EOLComment();
                setNextLex();
            } else if (t.getCh() == '*') {
                t.setNextCh();
                commentHandler.traditionalComment();
                setNextLex();
            }
        }
    }

    private boolean findAssignLex(Scanner scanner) {
        return ((lex = scanner.scan()) != Lex.NONE);
    }

    private void nextLex() {
        for (Scanner scanner : scanners) {
            if (findAssignLex(scanner)) {
                break;
            }
        }
    }

    public void setNextLex() {
        skipSpace();
        lex = Lex.NONE;
        nextLex();
        checkEOT();
        checkComment();

    }

    public Lex getLex() {
        return lex;
    }

    public Text getT() {
        return t;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIntNum() {
        return intNum;
    }

    public void setIntNum(int intNum) {
        this.intNum = intNum;
    }

    public String getStrLit() {
        return strLit;
    }

    public void setStrLit(String strLit) {
        this.strLit = strLit;
    }
}
