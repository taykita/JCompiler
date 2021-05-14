package JCompiler;

import JCompiler.Exceptions.ScannerExceptions.FileException;
import JCompiler.Scanner.LiteralScanner.CharacterSeq;
import JCompiler.Utils.IO.In;
import JCompiler.Utils.IO.Out;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Text {
    private final Out out = new Out();

    private final char chEOT = '\0';
    private final char chEOL = '\n';
    private final char chSpace = ' ';
    private final char chTab = '\t';

    private String src = "";
    private int pos = 0;
    private int inLinePos = 0;
    private int prevLinePos = 0;
    private StringBuilder lines = new StringBuilder("");
    private int bSlashCount = 0;

    private final CharacterSeq characterSeq = new CharacterSeq(this);

    private char ch = chEOT;

    public void reset(String fileName) {
        try (FileReader file = new FileReader(fileName)) {
            BufferedReader fReader = new BufferedReader(file);

            StringBuilder fileText = new StringBuilder();
            while (fReader.ready()) {
                fileText.append(fReader.readLine()).append("\n");
            }
            src += fileText.toString();

        } catch (FileNotFoundException e) {
            throw new FileException("Файл не найден.");
        } catch (IOException e) {
            throw new FileException("Ошибка чтения файла");
        } catch (Exception e) {
            throw new FileException("Ошибка при закритии файла");
        }
    }

    public char getCh() {
        return ch;
    }

    public int getInLinePos() {
        return inLinePos;
    }

    public String getLines() {
        return lines.toString();
    }

    public int getPrevLinePos() {
        return prevLinePos;
    }

    public void setNextCh() {
        if (pos < src.length()) {
            ch = src.charAt(pos);

            // Вывод текста. Убрать/закоментить на этапе релиза
            //System.out.print(ch);

            lines.append(ch);
            pos++;
            inLinePos++;
            if ((ch == '\n') || (ch == '\r')) {
                ch = chEOL;
                prevLinePos = inLinePos;
                inLinePos = 0;
            }

            if (ch == '\\') {
                bSlashCount++;
                if ((pos < src.length()) && (bSlashCount == 1)) {
                    if (src.charAt(pos) == 'u') {
                        setNextCh();
                        ch = characterSeq.getUnicodeChar();
                    }
                } else {
                    bSlashCount = 0;
                }
            } else {
                bSlashCount = 0;
            }
        } else {
            ch = chEOT;
        }
    }

    public boolean chIsEOT() {
        return ch == chEOT;
    }

    public boolean chNotEOL() {
        return ch != chEOL;
    }

    public boolean chIsChar() {
        return (ch != chSpace) && (ch != chTab) && (ch != chEOL);
    }

    private List<String> getFiles(int count) {
        List<String> files = new ArrayList<>();
        In in = new In();

        for (int i = 0; i < count; i++) {
            out.out((i + 1) + " - ");
            files.add(in.inLn());
        }

        return files;
    }

    private List<String> getFiles(String[] args) {
        List<String> files = new ArrayList<>();

        if (args[0].charAt(0) == '*') {
            File file = new File(System.getProperty("user.dir"));
            List<String> dirFiles = new ArrayList<>(Arrays.asList(file.list()));
            for (String name : dirFiles) {
                boolean trueFile = true;
                int l = args[0].length() - 1;
                for (int i = name.length() - 1; i >= 0; i--) {
                    if (name.charAt(i) == args[0].charAt(l)) {
                        if (args[0].charAt(l) == '.') {
                            break;
                        }
                        l--;
                    } else {
                        trueFile = false;
                        break;
                    }
                }
                if (args[0].charAt(l) != '.') {
                    trueFile = false;
                }
                if (trueFile) {
                    files.add(name);
                }
            }
        } else {
            files.addAll(Arrays.asList(args));
        }

        return files;
    }

    public void addFiles(int count) {
        out.outLn("Введите путь к файлу/ам");
        for (String name : getFiles(count)) {
            reset(name);
        }
    }

    public void addFiles(String[] args) {
        for (String name : getFiles(args)) {
            reset(name);
        }
    }

}
