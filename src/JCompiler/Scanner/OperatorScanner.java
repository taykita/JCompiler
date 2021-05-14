package JCompiler.Scanner;

import JCompiler.Text;

public class OperatorScanner implements Scanner{
    public OperatorScanner(Text text) {
        this.t = text;
    }

    Text t;

    @Override
    public Lex scan() {
        Lex lex = Lex.NONE;
        char ch = t.getCh();

        if (ch == '=') {
            t.setNextCh();
            if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.EQ;
            } else {
                lex = Lex.ASSIGN;
            }
        } else if (ch == '>') {
            t.setNextCh();
            if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.GE;
            } else if (t.getCh() == '>') {
                t.setNextCh();
                if (t.getCh() == '>') {
                    t.setNextCh();
                    if (t.getCh() == '=') {
                        t.setNextCh();
                        lex = Lex.Z_R_SHIFT_ASSIGN;
                    } else {
                        lex = Lex.Z_R_SHIFT;
                    }
                } else if (t.getCh() == '=') {
                    t.setNextCh();
                    lex = Lex.R_SHIFT_ASSIGN;
                } else {
                    lex = Lex.R_SHIFT;
                }
            } else {
                lex = Lex.GT;
            }
        } else if (ch == '<') {
            t.setNextCh();
            if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.LE;
            } else if (t.getCh() == '<') {
                t.setNextCh();
                if (t.getCh() == '=') {
                    t.setNextCh();
                    lex = Lex.L_SHIFT_ASSIGN;
                } else {
                    lex = Lex.L_SHIFT;
                }
            } else {
                lex = Lex.LT;
            }
        } else if (ch == '!') {
            t.setNextCh();
            if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.NE;
            } else {
                lex = Lex.NOT;
            }
        } else if (ch == '~') {
            lex = Lex.TILDE;
            t.setNextCh();
        } else if (ch == '?') {
            lex = Lex.QMARK;
            t.setNextCh();
        } else if (ch == ':') {
            t.setNextCh();
            if (t.getCh() == ':') {
                t.setNextCh();
                lex = Lex.DOUBLE_COLON;
            } else {
                lex = Lex.COLON;
            }
        } else if (ch == '-') {
            t.setNextCh();
            if (t.getCh() == '>') {
                t.setNextCh();
                lex = Lex.R_ARROW;
            } else if (t.getCh() == '-') {
                t.setNextCh();
                lex = Lex.DEC;
            } else if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.MINUS_ASSIGN;
            } else {
                lex = Lex.MINUS;
            }
        } else if (ch == '&') {
            t.setNextCh();
            if (t.getCh() == '&') {
                t.setNextCh();
                lex = Lex.DOUBLE_AND;
            } else if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.AND_ASSIGN;
            } else {
                lex = Lex.AND;
            }
        } else if (ch == '|') {
            t.setNextCh();
            if (t.getCh() == '|') {
                t.setNextCh();
                lex = Lex.DOUBLE_OR;
            } else if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.OR_ASSIGN;
            } else {
                lex = Lex.OR;
            }
        } else if (ch == '+') {
            t.setNextCh();
            if (t.getCh() == '+') {
                t.setNextCh();
                lex = Lex.INC;
            } else if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.PLUS_ASSIGN;
            } else {
                lex = Lex.PLUS;
            }
        } else if (ch == '*') {
            t.setNextCh();
            if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.MULT_ASSIGN;
            } else {
                lex = Lex.MULT;
            }
        } else if (ch == '/') {
            t.setNextCh();
            if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.DIV_ASSIGN;
            } else {
                lex = Lex.DIV;
            }
        } else if (ch == '^') {
            t.setNextCh();
            if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.XOR_ASSIGN;
            } else {
                lex = Lex.XOR;
            }
        } else if (ch == '%') {
            t.setNextCh();
            if (t.getCh() == '=') {
                t.setNextCh();
                lex = Lex.PERCENT_ASSIGN;
            } else {
                lex = Lex.PERCENT;
            }
        }
        return lex;
    }

}
