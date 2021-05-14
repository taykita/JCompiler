package JCompiler.Scanner.LiteralScanner;

import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Scanner.Scanner;
import JCompiler.Scanner.Lex;
import JCompiler.Text;

public class StringLitScanner extends CharacterSeq implements Scanner {
    public StringLitScanner(Text text) {
        super(text);
        this.t = text;
    }

    Text t;

    @Override
    public Lex scan() {
        t.setNextCh();
        while (t.getCh() != '\"') {
            if (t.chIsEOT() || !t.chNotEOL()) {
                throw new LexException(t, "Не закончен строковой литерал");
            }
            skipChar();
        }
        t.setNextCh();
        return Lex.STRING_LIT;
    }
}
