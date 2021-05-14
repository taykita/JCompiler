package JCompiler.Scanner.LiteralScanner;

import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Scanner.Scanner;
import JCompiler.Text;
import JCompiler.Scanner.Lex;

public class ChLitScanner extends CharacterSeq implements Scanner {
    public ChLitScanner(Text text) {
        super(text);
        this.t = text;
    }

    Text t;


    @Override
    public Lex scan() {
        t.setNextCh();
        if (t.getCh() != '\'') {
            skipChar();
        }
        if (t.getCh() != '\'') {
            throw new LexException(t, "Ожидается [ \' ]");
        } else {
            t.setNextCh();
        }

        return Lex.CH_LIT;
    }
}
