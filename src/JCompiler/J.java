package JCompiler;

import JCompiler.Exceptions.ParserExceptions.ParsException;
import JCompiler.Exceptions.ScannerExceptions.FileException;
import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.JVM.Gen;
import JCompiler.JVM.JVM;
import JCompiler.Scanner.MainScanner;
import JCompiler.Utils.IO.Out;
import JCompiler.Parser.MainParser;

public class J {
    public static Text t = new Text();
    private static final Out out = new Out();

    public static void addFilesFromArgs(Text t, String[] args) {
        try {
            t.addFiles(args);
        } catch (FileException e) {
            out.outLn(e.getErrorMsg());
            System.exit(1);
            //addFilesFromConsole(t);
        }
    }

    public static void addFiles(Text t, String[] args) {
        if (args.length != 0)
            addFilesFromArgs(t, args);
    }

    public static void Init(Text t, String[] args) {
        addFiles(t, args);
    }


    public static void main(String[] args) {
        Init(t, args);
        MainScanner scanner = new MainScanner(t);
        int PC = 0;
        JVM jvm = new JVM(PC);
        Gen gen = new Gen(PC, jvm.getM());
        MainParser pars = new MainParser(scanner, jvm, gen, PC);
        out.outLn("Компилятор языка \"JavaScanner.J\"");
        try {
            pars.Compile();
            out.outLn("Компиляция завершена");
            //jvm.printCode(gen.getPC());
            jvm.Run();
        } catch (ParsException e) {
            e.showError();
            //e.showStackTrace();
        } catch (LexException e) {
            e.showError();
        }
    }
}
