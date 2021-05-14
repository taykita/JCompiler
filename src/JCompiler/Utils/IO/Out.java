package JCompiler.Utils.IO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Out implements OutInterface {

    @Override
    public void out(Object line) {
        System.out.print(line);
    }

    @Override
    public void outLn(Object line) {
        System.out.println(line);
    }

    public void outF(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList(args));
        String format = args[0];
        list.remove(args[0]);
        args = list.toArray(new String[list.size()]);

        System.out.printf(format, args);
    }

}
