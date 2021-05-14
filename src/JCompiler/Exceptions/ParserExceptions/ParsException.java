package JCompiler.Exceptions.ParserExceptions;

import JCompiler.Text;
import JCompiler.Utils.IO.Out;

public class ParsException extends RuntimeException {
    public ParsException() {

    }

    public ParsException(Text t, String msg) {
        super(msg);
        this.t = t;
        this.msg = msg;
    }

    private String msg;
    private Text t;


    private int getPos() {
        if (t.getInLinePos() - 1 < 0) {
            return t.getPrevLinePos();
        }
        return t.getInLinePos() - 1;
    }

    public void showError() {
        Out print = new Out();
        print.out(t.getLines());
        print.outLn("");
        print.out(new String(new char[getPos()]).replace("\0", " "));
        print.outLn("^");
        print.outLn(msg);
    }

    public void showStackTrace() {
        for (int i = 0; i < getStackTrace().length; i++) {
            Out print = new Out();
            print.out(new String(new char[i]).replace("\0", " "));
            print.outLn(getStackTrace()[i]);
        }
    }

    public String getErrorMsg() {
        return msg;
    }
}
