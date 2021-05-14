package JCompiler.Scanner;

import java.util.HashMap;

public class DictionaryJavaLex extends HashMap<String, Lex> {
    public DictionaryJavaLex() {
        this.put("abstract", Lex.ABSTRACT);
        this.put("continue", Lex.CONTINUE);
        this.put("for", Lex.FOR);
        this.put("new", Lex.NEW);
        this.put("switch", Lex.SWITCH);
        this.put("assert", Lex.ASSERT);
        this.put("default", Lex.DEFAULT);
        this.put("if", Lex.IF);
        this.put("package", Lex.PACKAGE);
        this.put("synchronized", Lex.SYNCHRONIZED);
        this.put("boolean", Lex.BOOLEAN);
        this.put("do", Lex.DO);
        this.put("goto", Lex.GOTO);
        this.put("private", Lex.PRIVATE);
        this.put("this", Lex.THIS);
        this.put("break", Lex.BREAK);
        this.put("double", Lex.DOUBLE);
        this.put("implements", Lex.IMPLEMENTS);
        this.put("protected", Lex.PROTECTED);
        this.put("throw", Lex.THROW);
        this.put("byte", Lex.BYTE);
        this.put("else", Lex.ELSE);
        this.put("import", Lex.IMPORT);
        this.put("public", Lex.PUBLIC);
        this.put("throws", Lex.THROWS);
        this.put("case", Lex.CASE);
        this.put("enum", Lex.ENUM);
        this.put("instanceof", Lex.INSTANCEOF);
        this.put("return", Lex.RETURN);
        this.put("transient", Lex.TRANSIENT);
        this.put("catch", Lex.CATCH);
        this.put("extends", Lex.EXTENDS);
        this.put("int", Lex.INT);
        this.put("short", Lex.SHORT);
        this.put("try", Lex.TRY);
        this.put("char", Lex.CHAR);
        this.put("final", Lex.FINAL);
        this.put("interface", Lex.INTERFACE);
        this.put("static", Lex.STATIC);
        this.put("void", Lex.VOID);
        this.put("class", Lex.CLASS);
        this.put("finally", Lex.FINALLY);
        this.put("long", Lex.LONG);
        this.put("strictfp", Lex.STRICTFP);
        this.put("volatile", Lex.VOLATILE);
        this.put("const", Lex.CONST);
        this.put("float", Lex.FLOAT);
        this.put("native", Lex.NATIVE);
        this.put("super", Lex.SUPER);
        this.put("while", Lex.WHILE);
        this.put("true", Lex.BOOLEAN_LIT);
        this.put("false", Lex.BOOLEAN_LIT);
        this.put("null", Lex.NULL_LIT);
    }
}