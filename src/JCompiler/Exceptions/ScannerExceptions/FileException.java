package JCompiler.Exceptions.ScannerExceptions;

public class FileException extends ScannerException{
    public FileException() {

    }

    public FileException(String msg) {
        super("Ошибка в обработке файла! " + msg);
    }

}
