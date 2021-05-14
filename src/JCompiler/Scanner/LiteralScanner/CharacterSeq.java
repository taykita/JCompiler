package JCompiler.Scanner.LiteralScanner;

import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CharacterSeq {
    public CharacterSeq(Text text) {
        this.t = text;
    }

    Text t;

    private final Set<Character> ESCAPE_SEQ = new HashSet<>(Arrays.asList('b', 't', 'n', 'f', 'r', '\"', '\'', '\\'));

    private final Set<Character> HEX_DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F'));

    private final Set<Character> OCTAL_DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7'));

    private final Set<Character> ZERO_TO_THREE = new HashSet<>(Arrays.asList('0', '1', '2', '3'));

    public char getUnicodeChar() {
        char uChar = 'u';
        StringBuilder tmpChar = new StringBuilder("\\");
        if (t.getCh() == 'u') {
            for (int i = 0; i < 4; i++) {
                tmpChar.append(t.getCh());
                t.setNextCh();
                if (!HEX_DIGIT.contains(t.getCh())) {
                    throw new LexException(t, "Неверный символ Unicode-последовательности");
                }
            }
            tmpChar.append(t.getCh());

        }
        String tmp = tmpChar.toString();
        if (tmp.startsWith("\\u"))
            uChar = String.valueOf((char)Integer.parseInt(tmp.substring(2), 16)).charAt(0);
        return uChar;
    }
/*
    public boolean isUnicodeEscape() {
        if (t.getCh() == 'u') {
            boolean answer = true;
            for (int i = 0; i < 4; i++) {
                t.setNextCh();
                if (!HEX_DIGIT.contains(t.getCh())) {
                    answer = false;
                    break;
                }
            }
            if (!answer) {
                throw new LexException(t, "Неверный символ Unicode-последовательности");
            }
            t.setNextCh();
            return true;
        }
        return false;
    }*/

    public boolean isOctalEscape() {
        boolean answer = false;

        if (ZERO_TO_THREE.contains(t.getCh())) {
            answer = true;
            t.setNextCh();
            if (OCTAL_DIGIT.contains(t.getCh())) {
                t.setNextCh();
                if (OCTAL_DIGIT.contains(t.getCh())) {
                    t.setNextCh();
                }
            }
        } else if (OCTAL_DIGIT.contains(t.getCh())) {
            answer = true;
            t.setNextCh();
            if (OCTAL_DIGIT.contains(t.getCh())) {
                t.setNextCh();
            }
        }

        return answer;
    }

    public boolean isNormalEscape(){
        if (ESCAPE_SEQ.contains(t.getCh())) {
            t.setNextCh();
            return true;
        }
        return false;
    }

    public boolean isEscapeSeq() {
        if (t.getCh() == '\\') {
            t.setNextCh();
            if (!(isNormalEscape() || isOctalEscape())) {
                throw new LexException(t, "Неверный символ управляющей последовательности");
            }
            return true;
        }
        return false;
    }

    public void skipChar() {
        if (!isEscapeSeq()) {
            t.setNextCh();
        }
    }

}
