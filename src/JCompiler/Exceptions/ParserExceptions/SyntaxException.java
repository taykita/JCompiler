package JCompiler.Exceptions.ParserExceptions;

import JCompiler.Text;

public class SyntaxException extends ParsException {
    public SyntaxException() {

    }

    public SyntaxException(Text t, String msg) {
        super(t, "Синтаксическая ошибка! " + msg);
    }
}
