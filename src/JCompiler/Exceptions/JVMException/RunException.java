package JCompiler.Exceptions.JVMException;

import JCompiler.Exceptions.ParserExceptions.ParsException;
import JCompiler.J;

public class RunException extends JVMException {
    public RunException() {

    }

    public RunException(String msg) {
        super("Ошибка интерпретатора! " + msg);
    }

}
