package JCompiler.Exceptions.ParserExceptions;

import JCompiler.J;
import JCompiler.Text;

public class ContextException extends ParsException {
    public ContextException() {

    }

    public ContextException(String msg) {
        super(J.t, "Контекстная ошибка! " + msg);
    }

}
