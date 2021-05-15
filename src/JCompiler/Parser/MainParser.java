package JCompiler.Parser;

import JCompiler.Exceptions.ParserExceptions.ContextException;
import JCompiler.JVM.Gen;
import JCompiler.JVM.JVM;
import JCompiler.Parser.Items.*;
import JCompiler.Scanner.MainScanner;
import JCompiler.Scanner.Lex;
import JCompiler.Exceptions.ParserExceptions.SyntaxException;

import java.util.*;

public class MainParser {
    public MainParser(MainScanner scan, JVM jvm, Gen gen, int PC) {
        this.scan = scan;
        this.jvm = jvm;
        this.gen = gen;
        this.PC = PC;
    }

    private Table table = new Table();
    private Set<String> procedures = new HashSet<>(Arrays.asList("System.exit", "System.out.println"));

    public enum T {
        Int, Bool
    }

    private int PC;
    private final JVM jvm;
    private final Gen gen;

    private final MainScanner scan;

    private void check(Lex lex) {
        if (scan.getLex() != lex) {
            throw new SyntaxException(scan.getT(), "Ожидается " + lex.name());
        }
    }

    private void skip(Lex lex) {
        if (scan.getLex() == lex) {
            scan.setNextLex();
        } else {
            throw new SyntaxException(scan.getT(), "Ожидается " + lex.name());
        }
    }

    //    ОбъявлПерем	= Тип, Имя, ["=", ПростоеВыраж] {"," Имя, ["=", КонстВыраж]} ";"
//    Тип = "int";
    private void VarDeclaration() {
        skip(Lex.INT);
        check(Lex.NAME);
        table.set(new Var(scan.getName(), T.Int), scan.getName());
        Var x = (Var) table.find(scan.getName());
        scan.setNextLex();
        if (scan.getLex() == Lex.ASSIGN) {
            gen.genAddr(x);
            scan.setNextLex();
            intExpr();
            gen.gen(jvm.SAVE);
        }
        while (scan.getLex() == Lex.COMMA) {
            scan.setNextLex();
            check(Lex.NAME);
            table.set(new Var(scan.getName(), T.Int), scan.getName());
            x = (Var) table.find(scan.getName());
            scan.setNextLex();
            if (scan.getLex() == Lex.ASSIGN) {
                gen.genAddr(x);
                scan.setNextLex();
                intExpr();
                gen.gen(jvm.SAVE);
            }
        }
        check(Lex.SEMI);
    }

    private void intExpr() {
        T currentType = Expression();
        if (currentType != T.Int) {
            throw new ContextException("Ожидается выражение целого типа");
        }
    }

    private void Function(Func x) {
        switch (x.getName()) {
            case "Math.abs":
                intExpr();
                gen.gen(jvm.DUP);
                gen.gen(0);
                gen.gen(gen.getPC() + 3);
                gen.gen(jvm.IFGE);
                gen.gen(jvm.NEG);
                break;
            case "Math.max":
                intExpr();
                gen.gen(jvm.DUP);
                skip(Lex.COMMA);
                intExpr();
                gen.gen(jvm.SWAP);
                ;
                gen.gen(jvm.OVER);
                gen.gen(gen.getPC() + 3);
                gen.gen(jvm.IFGE);
                gen.gen(jvm.SWAP);
                ;
                gen.gen(jvm.DROP);
                //gen.gen(PC + 3);
                break;
            case "Math.min":
                intExpr();
                gen.gen(jvm.DUP);
                skip(Lex.COMMA);
                intExpr();
                gen.gen(jvm.SWAP);
                ;
                gen.gen(jvm.OVER);
                gen.gen(gen.getPC() + 3);
                gen.gen(jvm.IFLE);
                gen.gen(jvm.SWAP);
                ;
                gen.gen(jvm.DROP);
                break;
            case "Math.signum":
                gen.gen(1);
                intExpr();
                gen.gen(0);
                gen.gen(gen.getPC() + 3);
                gen.gen(jvm.IFGE);
                gen.gen(jvm.NEG);
                break;
            default:
                assert false;
                break;
        }
    }

    //    Множитель =
//        Имя ["(" Выраж | Тип ")"]
//        | Число
//        | "(" Выраж ")".
    private T Factor() {
        if (scan.getLex() == Lex.NAME) {
            Object x = table.find(scan.getName());
            if (x instanceof Const) {
                gen.genConst((Integer) ((Const<?>) x).getValue());
                scan.setNextLex();
                return ((Const<?>) x).getType();
            } else if (x instanceof Var) {
                gen.genAddr((Var) x);
                gen.gen(jvm.LOAD);
                scan.setNextLex();
                return ((Var<?>) x).getType();
            } else if (x instanceof Func) {
                scan.setNextLex();
                skip(Lex.L_PAR);
                Function((Func) x);
                skip(Lex.R_PAR);
                return ((Func) x).getType();
            } else if (x instanceof JCompiler.Parser.Items.Class) {
                scan.setNextLex();
                skip(Lex.DOT);
                check(Lex.NAME);
                String key = ((JCompiler.Parser.Items.Class) x).getName() + '.' + scan.getName();
                x = table.find(key);
                if (!(x instanceof Func)) {
                    throw new ContextException("Ожидается функция");
                }
                scan.setNextLex();
                skip(Lex.L_PAR);
                Function((Func) x);
                skip(Lex.R_PAR);
                return ((Func) x).getType();
            } else {
                throw new ContextException("Ожидается имя константы, переменной или функции");
            }
        } else if (scan.getLex() == Lex.INT_LIT) {
            gen.gen(scan.getIntNum());
            scan.setNextLex();
            return T.Int;
        } else if (scan.getLex() == Lex.L_PAR) {
            scan.setNextLex();
            T currentType = Expression();
            skip(Lex.R_PAR);
            return currentType;
        } else {
            throw new SyntaxException(scan.getT(), "Ожидается имя, число или '('");
        }
    }

    //    Слагаемое = Множитель {ОперУмн Множитель}.
    private T Term() {
        T currentType;
        currentType = Factor();
        while ((scan.getLex() == Lex.MULT) || (scan.getLex() == Lex.DIV) || (scan.getLex() == Lex.PERCENT)) {
            Lex op = scan.getLex();
            testInt(currentType);
            scan.setNextLex();
            currentType = Factor();
            testInt(currentType);
            if (op == Lex.DIV) {
                gen.gen(jvm.DIV);
            } else if (op == Lex.MULT) {
                gen.gen(jvm.MULT);
            } else {
                gen.gen(jvm.MOD);
            }
        }
        return currentType;
    }

    private void testInt(T type) {
        if (type != T.Int) {
            throw new ContextException("Ожидается выражение целого типа");
        }
    }

    //    Выраж = ["+"|"-"] Слагаемое {ОперСлож Слагаемое}.
    private T SimpleExpression() {
        T currentType;
        if ((scan.getLex() == Lex.PLUS) || (scan.getLex() == Lex.MINUS)) {
            Lex op = scan.getLex();
            scan.setNextLex();
            currentType = Term();
            testInt(currentType);
            if (op == Lex.MINUS) {
                gen.gen(jvm.NEG);
            }
        } else {
            currentType = Term();
        }
        while ((scan.getLex() == Lex.PLUS) || (scan.getLex() == Lex.MINUS)) {
            Lex op = scan.getLex();
            testInt(currentType);
            scan.setNextLex();
            currentType = Term();
            testInt(currentType);
            if (op == Lex.PLUS) {
                gen.gen(jvm.ADD);
            } else {
                gen.gen(jvm.SUB);
            }
        }
        return currentType;
    }

    //    Выраж = ПростоеВыраж [Отношение ПростоеВыраж].
    private T Expression() {
        T currentType = SimpleExpression();
        if (new HashSet<Lex>(Arrays.asList(Lex.EQ, Lex.NE, Lex.LT, Lex.LE, Lex.GT, Lex.GE)).contains(scan.getLex())) {
            Lex op = scan.getLex();
            testInt(currentType);
            scan.setNextLex();
            currentType = SimpleExpression();
            testInt(currentType);
            gen.genComp(op);
            return T.Bool;
        } else {
            return currentType;
        }
    }

    //    КонстВыраж = ["+" | "-"] (Число | Имя).
    private int ConstExpression() {
        int sign = 1;
        if ((scan.getLex() == Lex.PLUS) || (scan.getLex() == Lex.MINUS)) {
            if (scan.getLex() == Lex.MINUS) {
                sign = -1;
            }
            scan.setNextLex();
        }
        if (scan.getLex() == Lex.INT_LIT) {
            int value = scan.getIntNum() * sign;
            scan.setNextLex();
            return value;
        } else if (scan.getLex() == Lex.NAME) {
            Object x = table.find(scan.getName());
            scan.setNextLex();
            if (x instanceof Const) {
                if (((Const<?>) x).getType() == T.Int) {
                    return ((Const<Integer>) x).getValue() * sign;
                }
            } else {
                throw new ContextException("Ожидается константа");
            }
        } else {
            throw new SyntaxException(scan.getT(), "Ожидается число или имя");
        }
        return 0;
    }

    //    ОбъявлКонст = "const" Тип, Имя, "=", КонстВыраж ";"
    private void ConstDeclaration() {
        skip(Lex.FINAL);
        skip(Lex.INT);

        check(Lex.NAME);
        String name = scan.getName();
        scan.setNextLex();

        skip(Lex.ASSIGN);
        int value = ConstExpression();
        table.set(new Const<Integer>(name, T.Int, value), name);

        check(Lex.SEMI);
    }

    //    Параметр = Переменная | Выраж.
    private void Parameter() {
        Expression();
    }

    private void AssStatement(Var x) {
        gen.genAddr(x);
        skip(Lex.NAME);
        skip(Lex.ASSIGN);
        T currentType = Expression();
        if (x.getType() != currentType) {
            throw new ContextException("Несоответствие типов присваивания");
        }
        gen.gen(jvm.SAVE);
    }

    private void Variable() {
        check(Lex.NAME);
        Object v = table.find(scan.getName());
        if (!(v instanceof Var)) {
            throw new ContextException("Ожидается имя переменной");
        }
        gen.genAddr((Var) v);
        scan.setNextLex();
    }

    private void Procedure(Proc x) {
        if (x.getName().equals("System.exit")) {
            int value = ConstExpression();
            gen.genConst(value);
            gen.gen(jvm.STOP);
        } else if (x.getName().equals("System.out.println")) {
            if (scan.getLex() != Lex.R_PAR) {
                intExpr();
                gen.gen(jvm.OUTLN);
            } else {
                gen.gen(jvm.LN);
            }
        } else if (x.getName().equals("System.out.printf")) {
            if (scan.getLex() != Lex.R_PAR) {
                check(Lex.STRING_LIT);
                String format = scan.getStrLit();
                scan.setNextLex();
                gen.gen(format.hashCode());
                intExpr();
                gen.gen(jvm.OUTLN);
            } else {
                gen.gen(jvm.LN);
            }
        } else if (x.getName().equals("System.out.print")) {
            if (scan.getLex() != Lex.R_PAR) {
                intExpr();
                gen.gen(jvm.OUTLN);
            } else {
                gen.gen(jvm.LN);
            }
        } else {
            Variable();
            gen.gen(jvm.IN);
            gen.gen(jvm.SAVE);
        }
    }

    private void CallStatement(Object x) {
        skip(Lex.NAME);
        if (scan.getLex() == Lex.DOT) {
            if (x instanceof JCompiler.Parser.Items.Class) {
                scan.setNextLex();
                check(Lex.NAME);
                String key = ((JCompiler.Parser.Items.Class) x).getName() + '.' + scan.getName();
                Object y = table.find(key);
                if (y instanceof JCompiler.Parser.Items.Class) {
                    scan.setNextLex();
                    skip(Lex.DOT);
                    check(Lex.NAME);
                    key += '.' + scan.getName();
                }
                x = table.find(key);
                if (!(x instanceof Proc)) {
                    throw new ContextException("Ожидается процедура");
                }
                scan.setNextLex();
            } else {
                throw new ContextException("Ожидается имя класса слева от точки");
            }
        } else if (!(x instanceof Proc)) {
            throw new ContextException("Ожидается имя процедуры");
        }
        if (scan.getLex() == Lex.L_PAR) {
            scan.setNextLex();
            Procedure((Proc) x);
            skip(Lex.R_PAR);
        } else if (procedures.contains(((Proc) x).getName())) {
            throw new ContextException("Ожидаются объявленные процедуры");
        }

    }

    private void CreateObjectStatement(Type x) {
        if (x.getName().equals("Scanner")) {
            scan.setNextLex();
            check(Lex.NAME);
            table.set(new JCompiler.Parser.Items.Class(scan.getName()), scan.getName());
            table.set(new Proc(scan.getName() + ".nextInt"), scan.getName() + ".nextInt");
            scan.setNextLex();
            skip(Lex.ASSIGN);
            skip(Lex.NEW);
            check(Lex.NAME);
            if (scan.getName().equals("Scanner")) {
                scan.setNextLex();
                skip(Lex.L_PAR);
                check(Lex.NAME);
                if (scan.getName().equals("System")) {
                    scan.setNextLex();
                    skip(Lex.DOT);
                    check(Lex.NAME);
                    if (scan.getName().equals("in")) {
                        scan.setNextLex();
                    } else {
                        throw new ContextException("Ожидается in");
                    }
                } else {
                    throw new ContextException("Ожидается System");
                }
                skip(Lex.R_PAR);
            } else {
                throw new ContextException("Ожидается имя Scanner");
            }
        } else {
            throw new ContextException("Неизвестное имя типа");
        }

    }

    //    Имя [{"." Имя}] "(" [Параметр {"," Параметр}]")" ";"
//    Переменная "=" Выраж ";"
    private void AssOrCallOrDecl() {
        check(Lex.NAME);
        Object x = table.find(scan.getName());

        if (x instanceof Var) {
            AssStatement((Var) x);
        } else if (x instanceof Proc || x instanceof JCompiler.Parser.Items.Class) {
            CallStatement(x);
        } else if (x instanceof Type) {
            CreateObjectStatement((Type) x);
        } else {
            throw new ContextException("Ожидается имя переменной или процедуры");
        }

    }

    private void testBool(T currentType) {
        if (currentType != T.Bool) {
            throw new ContextException("Ожидается логическое выражение");
        }
    }

    private void boolExr() {
        T currentType = Expression();
        testBool(currentType);
    }

    //    "if", "(", Выраж, ")",
//        (Оператор ";"
//        | "{" ПослОператоров "}")
//    {"else" "if", "(", Выраж, ")",
//        (Оператор ";"
//        | "{" ПослОператоров ";" "}")}
//    ["else",
//        (Оператор ";"
//        | "{" ПослОператоров ";" "}"]
    private void IfStatement() {
        skip(Lex.IF);
        skip(Lex.L_PAR);
        boolExr();
        skip(Lex.R_PAR);
        int CondPC = gen.getPC();
        int LastGOTO = 0;
        if (scan.getLex() == Lex.L_BRACE) {
            scan.setNextLex();
            SeqStatements();
            skip(Lex.R_BRACE);
        } else {
            Statement();
            check(Lex.SEMI);
        }

        while (scan.getLex() == Lex.ELSE) {
            scan.setNextLex();
            if (scan.getLex() == Lex.IF) {
                gen.gen(LastGOTO);
                gen.gen(jvm.GOTO);
                LastGOTO = gen.getPC();
                gen.fixup(CondPC, gen.getPC());
                skip(Lex.IF);
                skip(Lex.L_PAR);
                boolExr();
                skip(Lex.R_PAR);
                CondPC = gen.getPC();
                if (scan.getLex() == Lex.L_BRACE) {
                    scan.setNextLex();
                    SeqStatements();
                    skip(Lex.R_BRACE);
                } else {
                    Statement();
                    check(Lex.SEMI);
                }
                if (scan.getLex() != Lex.ELSE) {
                    gen.fixup(CondPC, gen.getPC());
                }
            } else {
                gen.gen(LastGOTO);
                gen.gen(jvm.GOTO);
                LastGOTO = gen.getPC();
                jvm.printCode(gen.getPC());
                gen.fixup(CondPC, gen.getPC());
                if (scan.getLex() == Lex.L_BRACE) {
                    scan.setNextLex();
                    SeqStatements();
                    skip(Lex.R_BRACE);
                } else {
                    Statement();
                    check(Lex.SEMI);
                }
            }
        }
        gen.fixup(LastGOTO, gen.getPC());

    }

    //    "while", "(", Выраж, ")",
//        (Оператор ";"
//        | "{" ПослОператоров "}")
    private void WhileStatement() {
        int WhilePC = gen.getPC();
        skip(Lex.WHILE);
        skip(Lex.L_PAR);
        boolExr();
        skip(Lex.R_PAR);
        int CondPC = gen.getPC();
        if (scan.getLex() == Lex.L_BRACE) {
            scan.setNextLex();
            SeqStatements();
            skip(Lex.R_BRACE);
        } else {
            Statement();
            check(Lex.SEMI);
        }
        gen.gen(WhilePC);
        gen.gen(jvm.GOTO);
        gen.fixup(CondPC, gen.getPC());
    }

    //    Оператор = [
//        ОбъявлПерем
//        | ОбъявлКонст
//        | Имя [{"." Имя}] "(" [Параметр {"," Параметр}]")" ";"
//        | "if", "(", Выраж, ")",
//            (Оператор ";"
//            | "{" ПослОператоров "}")
//        "else",
//            (Оператор ";"
//            | "{" ПослОператоров "}")
//        | "while", "(", Выраж, ")",
//            (Оператор ";"
//            | "{" ПослОператоров "}")
//        | Переменная "=" Выраж ";"
//    ]
    private void Statement() {
        if (scan.getLex() == Lex.INT) {
            VarDeclaration();
            skip(Lex.SEMI);
        } else if (scan.getLex() == Lex.FINAL) {
            ConstDeclaration();
            skip(Lex.SEMI);
        } else if (scan.getLex() == Lex.NAME) {
            AssOrCallOrDecl();
            skip(Lex.SEMI);
        } else if (scan.getLex() == Lex.IF) {
            IfStatement();
        } else if (scan.getLex() == Lex.WHILE) {
            WhileStatement();
        } else {
            throw new SyntaxException(scan.getT(), "Ожидается оператор");
        }
    }

    //    ПослОператоров =
//        Оператор ";"
//        {Если нет "}", то Оператор ";"}.
    private void SeqStatements() {
        {
            Statement();
            //skip(Lex.SEMI);
            while (scan.getLex() != Lex.R_BRACE) {
                Statement();
                //skip(Lex.SEMI);
            }
        }
    }


    //    MainClass	=	"public", "class", Identifier,
//    "{",
//        "public", "static", "void", "main", "(", ")",
//        "{",
//            ПослОператоров
//        "}",
//    "}";
    private void MainClass() {
        skip(Lex.PUBLIC);
        skip(Lex.CLASS);
        skip(Lex.NAME);
        table.set(new JCompiler.Parser.Items.Class(scan.getName()), scan.getName());
        skip(Lex.L_BRACE);

        skip(Lex.PUBLIC);
        skip(Lex.STATIC);
        skip(Lex.VOID);
        skip(Lex.NAME);
        skip(Lex.L_PAR);
        skip(Lex.R_PAR);
        skip(Lex.L_BRACE);
        SeqStatements();
        skip(Lex.R_BRACE);

        skip(Lex.R_BRACE);
    }

    private void EOF() {
        if (scan.getLex() != Lex.EOT) {
            throw new SyntaxException(scan.getT(), "Ожидается конец текста");
        }
        gen.gen(jvm.STOP);
        AllocVar();
    }

    //    Импорт = {"import" Name ["." Name {"." Name}] ";"}
    private void Import() {
        while (scan.getLex() == Lex.IMPORT) {
            scan.setNextLex();
            check(Lex.NAME);
            List<String> name = new ArrayList<>();
            if (scan.getName().equals("java")) {
                name.add(scan.getName());
                scan.setNextLex();
                skip(Lex.DOT);
                check(Lex.NAME);
                if (scan.getName().equals("util")) {
                    name.add(scan.getName());
                    scan.setNextLex();
                    skip(Lex.DOT);
                    check(Lex.NAME);
                    if (scan.getName().equals("Scanner")) {
                        name.add(scan.getName());
                        scan.setNextLex();
                        StringBuilder finalName = new StringBuilder();
                        for (String partName : name) {
                            finalName.append(partName);
                        }
                        table.set(new JCompiler.Parser.Items.Class(finalName.toString()), finalName.toString());
                    } else {
                        throw new ContextException("Ожидается Scanner");
                    }
                } else {
                    throw new ContextException("Ожидается util");
                }
            } else {
                throw new ContextException("Ожидается java");
            }

            skip(Lex.SEMI);
        }
    }

    //    Goal=
//        [Импорт]
//        MainClass
//        EOF
    private void Goal() {
        Import();
        MainClass();
        EOF();
    }

    private void AllocVar() {
        List<Var> vars = table.getVars();

        for (Var v : vars) {
            if (v.getAddr() > 0) {
                gen.fixup(v.getAddr(), gen.getPC());
                gen.gen(0);
            }
        }
    }

    public void Compile() {
        scan.setNextLex();
        //Items items = new Items();

        table.openScope();

        table.set(new Func("Math.abs", T.Int), "Math.abs");
        table.set(new Func("Math.max", T.Int), "Math.max");
        table.set(new Func("Math.min", T.Int), "Math.min");
        table.set(new Func("Math.signum", T.Int), "Math.signum");
        table.set(new Proc("System.exit"), "System.exit");
        table.set(new Proc("System.out.println"), "System.out.println");
        table.set(new Proc("System.out.printf"), "System.out.printf");
        table.set(new Proc("System.out.print"), "System.out.print");
        table.set(new JCompiler.Parser.Items.Class("Math"), "Math");
        table.set(new JCompiler.Parser.Items.Class("System"), "System");
        table.set(new JCompiler.Parser.Items.Class("System.out"), "System.out");
        table.set(new Type("Scanner"), "Scanner");

        table.openScope();

        Goal();

        table.closeScope();
        table.closeScope();

    }
}
