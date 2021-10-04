package JCompiler.Exceptions.JVMException;


import JCompiler.Text;
import JCompiler.Utils.IO.Out;

public class JVMException extends RuntimeException {
    public JVMException() {

    }

    public JVMException(String msg) {
        super(msg);
        this.msg = msg;
    }

    private String msg;


    /*private int getPos() {
        if (t.getLexLine() - 1 < 0) {
            return t.getLexLine();
        }
        return t.getLexLine() - 1;
    }*/

    public void showError() {
        Out print = new Out();
        print.outLn("");
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

