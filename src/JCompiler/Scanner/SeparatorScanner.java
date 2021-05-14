package JCompiler.Scanner;

import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Scanner.LiteralScanner.NumberLitScanner;
import JCompiler.Text;

public class SeparatorScanner implements Scanner{
    public SeparatorScanner(Text text, MainScanner scan) {
        this.t = text;
    }

    MainScanner scan;
    Text t;

    @Override
    public Lex scan() {
        Lex lex = Lex.NONE;
        char ch = t.getCh();

        if (ch == '(') {
            lex = Lex.L_PAR;
            t.setNextCh();
        } else if (ch == ')') {
            lex = Lex.R_PAR;
            t.setNextCh();
        } else if (ch == '{') {
            lex = Lex.L_BRACE;
            t.setNextCh();
        } else if (ch == '}') {
            lex = Lex.R_BRACE;
            t.setNextCh();
        } else if (ch == '[') {
            lex = Lex.L_SQR_BR;
            t.setNextCh();
        } else if (ch == ']') {
            lex = Lex.R_SQR_BR;
            t.setNextCh();
        } else if (ch == ';') {
            lex = Lex.SEMI;
            t.setNextCh();
        } else if (ch == ',') {
            lex = Lex.COMMA;
            t.setNextCh();
        } else if (ch == '.') {
            t.setNextCh();
            NumberLitScanner litScanner = new NumberLitScanner(t, scan);
            if (litScanner.getDIGIT().contains(t.getCh())) {
                litScanner.skipFloat();
                return Lex.FLOAT_POINT_LIT;
            }
            if (t.getCh() == '.') {
                t.setNextCh();
                if (t.getCh() == '.') {
                    t.setNextCh();
                    lex = Lex.ELL;
                } else {
                    throw new LexException(t, "Недопустимый символ");
                }
            } else {
                lex = Lex.DOT;
            }
        } else if (ch == '@') {
            t.setNextCh();
            lex = Lex.AT;
        }
        return lex;
    }

}
