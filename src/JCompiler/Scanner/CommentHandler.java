package JCompiler.Scanner;

import JCompiler.Exceptions.ScannerExceptions.LexException;
import JCompiler.Text;

public class CommentHandler {
    public CommentHandler(Text text) {
        this.t = text;
    }

    Text t;

    private void skipComment() {
        while ((t.getCh() != '*') && (!t.chIsEOT())) {
            t.setNextCh();
        }
    }

    private void endComment() {
        if (t.chIsEOT()) {
            throw new LexException(t, "Не закончен комментарий");
        } else {
            t.setNextCh();
        }
    }

    public void traditionalComment() {
        do {
            skipComment();
            endComment();
        } while (t.getCh() != '/');
        t.setNextCh();
    }

    private void skipLine() {
        while (t.chNotEOL()) {
            t.setNextCh();
        }
    }

    public void EOLComment() {
        skipLine();
    }

}
