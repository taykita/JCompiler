package JCompiler;

import JCompiler.Exceptions.ScannerExceptions.FileException;
import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Scanner.MainScanner;
import JCompiler.Scanner.Lex;
import JCompiler.Utils.IO.In;
import JCompiler.Utils.IO.Out;

import java.util.*;

class LexData {
    private int count;
    private String name;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class TestLexScanner {

    private static final Out out = new Out();

    public static void showWelcomeMsg() {
        out.outLn("Лексический анализатор языка программирования Java версии 8");
        out.outLn("Для запуска программы можно использовать аргументы " +
                "командной строки следующим образом:");
        out.outLn(">JavaScanner <входные файлы>");
    }

    public static void addFilesFromArgs(Text t, String[] args) {
        try {
            t.addFiles(args);
        } catch (FileException e) {
            out.outLn(e.getErrorMsg());
            System.exit(1);
            //addFilesFromConsole(t);
        }
    }

    public static void addFilesFromConsole(Text t) {
        try {
            out.outLn("Для дальнейшей работы программы");
            out.outLn("Введите количество файлов");
            int count;
            count = new In().inInt();
            t.addFiles(count);
        } catch (FileException e) {
            out.outLn(e.getErrorMsg());
            addFilesFromConsole(t);
        }
    }

    public static void addFiles(Text t, String[] args) {
        if (args.length != 0) {
            addFilesFromArgs(t, args);
        } /*else {
            out.outLn("Аргументы командной строки не не найдены");
            addFilesFromConsole(t);
        }*/
    }

    public static void showLexMapData(Map<Lex, LexData> lexMap) {
        for (LexData value : lexMap.values()) {
            out.outF(new String[]{"%-20s %-10s%n", value.getName(), String.valueOf(value.getCount())});
        }
    }

    public static void showLexCount(int lexCount) {
        out.outF(new String[]{"%-20s %-10s%n", "Число лексем:", String.valueOf(lexCount)});
    }

    public static void main(String[] args) {
        Text t = new Text();
        MainScanner scanner = new MainScanner(t);
        Map<Lex, LexData> lexMap = new HashMap<>();

        showWelcomeMsg();
        addFiles(t, args);

        int lexCount = 0;
        try {
            t.setNextCh();
            scanner.setNextLex();
            while (scanner.getLex() != Lex.EOT) {
                LexData data;
                if (lexMap.containsKey(scanner.getLex())) {
                    data = lexMap.get(scanner.getLex());
                    data.setCount(data.getCount() + 1);
                } else {
                    data = new LexData();
                    data.setCount(1);
                    data.setName(scanner.getLex().name());
                }
                lexMap.put(scanner.getLex(), data);
                lexCount++;
                scanner.setNextLex();
            }
        } catch (LexException e) {
            e.showError();
            System.exit(1);
        }
        showLexMapData(lexMap);
        showLexCount(lexCount);

    }
}
