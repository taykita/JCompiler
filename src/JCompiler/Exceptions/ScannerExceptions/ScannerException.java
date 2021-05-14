package JCompiler.Exceptions.ScannerExceptions;

public class ScannerException extends RuntimeException{
    public ScannerException() {

    }

    private String msg;

    public ScannerException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getErrorMsg() {
        return msg;
    }
}
