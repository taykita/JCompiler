package JCompiler.Scanner.LiteralScanner;

import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Scanner.MainScanner;
import JCompiler.Scanner.Scanner;
import JCompiler.Scanner.Lex;
import JCompiler.Text;

public class StringLitScanner extends CharacterSeq implements Scanner {
    public StringLitScanner(Text text, MainScanner scan) {
        super(text);
        this.t = text;
        this.scan = scan;
    }

    private Text t;
    private MainScanner scan;

    @Override
    public Lex scan() {
        t.setNextCh();
        StringBuilder strLit = new StringBuilder();
        strLit.append(t.getCh());
        while (t.getCh() != '\"') {
            if (t.chIsEOT() || !t.chNotEOL()) {
                throw new LexException(t, "Не закончен строковой литерал");
            }
            skipChar();
            strLit.append(t.getCh());
        }
        strLit.deleteCharAt(strLit.length()-1);
        t.setNextCh();
        scan.setStrLit(strLit.toString());

        return Lex.STRING_LIT;
    }
}
