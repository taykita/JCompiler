package JCompiler.Scanner;

import JCompiler.Text;

import static java.lang.Character.*;

public class NameScanner implements Scanner{
    public NameScanner(Text text, MainScanner scan) {
        this.t = text;
        this.scan = scan;
    }

    private final DictionaryJavaLex keyWords = new DictionaryJavaLex();

    private MainScanner scan;
    Text t;

    private String getName() {
        StringBuilder name = new StringBuilder("" + t.getCh());

        t.setNextCh();
        while (isJavaIdentifierPart(t.getCh())) {
            name.append(t.getCh());
            t.setNextCh();
        }

        return name.toString();
    }

    @Override
    public Lex scan() {
        if (isJavaIdentifierStart(t.getCh())) {
            Lex nameLex = Lex.NAME;

            String name = getName();

            scan.setName(name);
            if (keyWords.containsKey(name)) {
                nameLex = keyWords.get(name);
            }

            return nameLex;
        } else {
            return Lex.NONE;
        }
    }
}
