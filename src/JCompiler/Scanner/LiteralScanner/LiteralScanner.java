package JCompiler.Scanner.LiteralScanner;

import JCompiler.Scanner.Lex;
import JCompiler.Scanner.MainScanner;
import JCompiler.Scanner.Scanner;
import JCompiler.Text;

import static java.lang.Character.isDigit;

public class LiteralScanner implements Scanner {
    public LiteralScanner(Text text, MainScanner scan) {
        this.t = text;
        this.chLitScanner = new ChLitScanner(this.t);
        this.stringLitScanner = new StringLitScanner(this.t);
        this.numberLitScanner = new NumberLitScanner(this.t, scan);
    }

    Text t;

    private final ChLitScanner chLitScanner;
    private final StringLitScanner stringLitScanner;
    private final NumberLitScanner numberLitScanner;

    public Lex scanCh() {
        return chLitScanner.scan();
    }

    public Lex scanString() {
        return stringLitScanner.scan();
    }

    public Lex scanNumber() {
        return numberLitScanner.scan();
    }

    @Override
    public Lex scan() {
        Lex lex = Lex.NONE;
        if (isDigit(t.getCh())) {
            lex = scanNumber();
        } else if (t.getCh() == '\'') {
            lex = scanCh();
        } else if (t.getCh() == '\"') {
            lex = scanString();
        }
        return lex;
    }
}
