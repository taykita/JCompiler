package JCompiler.Scanner.LiteralScanner;

import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Scanner.Lex;
import JCompiler.Scanner.MainScanner;
import JCompiler.Scanner.Scanner;
import JCompiler.Text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class NumberLitScanner implements Scanner {
    public NumberLitScanner(Text text, MainScanner scan) {
        this.t = text;
        this.scan = scan;
    }
    MainScanner scan;
    Text t;
    private String num;

    private final Set<Character> HEX_DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F'));
    private final Set<Character> HEX_DIGIT_AND_UNDERSCORE = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F', '_'));

    private final Set<Character> NON_ZERO_DIGIT = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
    private final Set<Character> DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    private final Set<Character> DIGIT_AND_UNDERSCORE = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_'));

    private final Set<Character> OCTAL_DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7'));
    private final Set<Character> OCTAL_DIGIT_AND_UNDERSCORE = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '_'));

    private final Set<Character> BINARY_DIGIT = new HashSet<>(Arrays.asList('0', '1'));
    private final Set<Character> BINARY_DIGIT_AND_UNDERSCORE = new HashSet<>(Arrays.asList('0', '1', '_'));

    private final Set<Character> EXPONENT_PART = new HashSet<>(Arrays.asList('e', 'E'));

    private final Set<Character> SIGN = new HashSet<>(Arrays.asList('+', '-'));

    private final Set<Character> INTEGER_TYPE_SUFFIX = new HashSet<>(Arrays.asList('l', 'L'));
    private final Set<Character> FLOAT_TYPE_SUFFIX = new HashSet<>(Arrays.asList('f', 'F', 'd', 'D'));


    private void skipUnderscores(Set<Character> numbers) {
        while (t.getCh() == '_') {
            t.setNextCh();
        }
        if (!numbers.contains(t.getCh())) {
            throw new LexException(t, "Ожидается цифра");
        }
    }

    private void skip(Set<Character> firstNumber, Set<Character> numbers, Set<Character> numbersAndUnderS) {
        if (firstNumber.contains(t.getCh())) {
            num += t.getCh();
            t.setNextCh();
            while (numbersAndUnderS.contains(t.getCh())) {
                skipUnderscores(numbers);
                num += t.getCh();
                t.setNextCh();
            }
        }
    }

    private void skipSignedInteger() {
        if (SIGN.contains(t.getCh())) {
            t.setNextCh();
        }
        if (DIGIT.contains(t.getCh())) {
            skip(DIGIT, DIGIT, DIGIT_AND_UNDERSCORE);
        } else {
            throw new LexException(t, "Некореректный литерал с плавающей точкой");
        }
    }

    public boolean isFloat() {
        if (t.getCh() == '.') {
            t.setNextCh();
            return true;
        } else return EXPONENT_PART.contains(t.getCh()) || FLOAT_TYPE_SUFFIX.contains(t.getCh());
    }

    public void skipFloat() {
        skip(DIGIT, DIGIT, DIGIT_AND_UNDERSCORE);

        if (EXPONENT_PART.contains(t.getCh())) {
            t.setNextCh();
            skipSignedInteger();
        }
        if (FLOAT_TYPE_SUFFIX.contains(t.getCh())) {
            t.setNextCh();
        }
    }

    private Lex getNumberLit() {
        num = "";
        if (t.getCh() == '0') {
            t.setNextCh();
            if (t.getCh() == 'x' || t.getCh() == 'X') {
                t.setNextCh();
                skip(HEX_DIGIT, HEX_DIGIT, HEX_DIGIT_AND_UNDERSCORE);
                scan.setIntNum(Integer.parseInt(num));
                if (isFloat()) {
                    skip(HEX_DIGIT, HEX_DIGIT, HEX_DIGIT_AND_UNDERSCORE);
                }
            } else if (t.getCh() == 'b' || t.getCh() == 'B') {
                t.setNextCh();
                skip(BINARY_DIGIT, BINARY_DIGIT, BINARY_DIGIT_AND_UNDERSCORE);
                scan.setIntNum(Integer.parseInt(num));
            } else if (OCTAL_DIGIT_AND_UNDERSCORE.contains(t.getCh())) {
                skipUnderscores(OCTAL_DIGIT);
                skip(OCTAL_DIGIT, OCTAL_DIGIT, OCTAL_DIGIT_AND_UNDERSCORE);
                scan.setIntNum(Integer.parseInt(num));
                if (DIGIT.contains(t.getCh())) {
                    skip(DIGIT, DIGIT, DIGIT_AND_UNDERSCORE);
                    if (isFloat()) {
                        skipFloat();
                        return Lex.FLOAT_POINT_LIT;
                    } else {
                        throw new LexException(t, "Неверное восьмеричное число");
                    }
                }
            }
            scan.setIntNum(0);
        } else {
            skip(NON_ZERO_DIGIT, DIGIT, DIGIT_AND_UNDERSCORE);
            scan.setIntNum(Integer.parseInt(num));
        }
        if (isFloat()) {
            skipFloat();
            return Lex.FLOAT_POINT_LIT;
        }

        if (INTEGER_TYPE_SUFFIX.contains(t.getCh())) {
            t.setNextCh();
        }



        return Lex.INT_LIT;
    }

    @Override
    public Lex scan() {
        return getNumberLit();
    }

    public Set<Character> getDIGIT() {
        return DIGIT;
    }
}
