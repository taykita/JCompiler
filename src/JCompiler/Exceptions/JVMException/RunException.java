package JCompiler.Exceptions.JVMException;

import JCompiler.Exceptions.ParserExceptions.ParsException;
import JCompiler.J;

public class RunException extends ParsException {
    public RunException() {

    }

    public RunException(String msg) {
        super(J.t, "Ошибка интерпретатора! " + msg);
    }

}
