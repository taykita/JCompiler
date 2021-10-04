package JCompiler.Exceptions.ScannerExceptions;

import JCompiler.Text;
import JCompiler.Utils.IO.Out;

public class LexException extends ScannerException{
    public LexException() {

    }

    public LexException(Text t, String msg) {
        super("Лексическая ошибка! " + msg);
        this.t = t;
        this.msg = "Лексическая ошибка! " + msg;
    }

    private Text t;
    private String msg;

    /*private int getPos() {
        if (t.getInLinePos() - 1 < 0) {
            return t.getPrevLinePos() - 1;
        }
        return t.getInLinePos() - 1;
    }*/

    public void showError() {
        Out print = new Out();
        print.out(t.getLines());
        print.outLn("");
        //print.out(new String(new char[getPos()]).replace("\0", " "));
        print.out(t.getLexLine());
        print.outLn("^");
        //print.outLn("В строке " + t.getLines() + " в столбце " + t.getInLinePos());
        print.outLn(msg);
    }

}
