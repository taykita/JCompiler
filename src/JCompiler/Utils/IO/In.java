package JCompiler.Utils.IO;

import java.util.Scanner;

public class In implements InInterface {

    Scanner read = new Scanner(System.in);

    @Override
    public String in() {
        return read.next();
    }

    @Override
    public int inInt() {
        return read.nextInt();
    }

    @Override
    public double inDouble() {
        return read.nextDouble();
    }

    @Override
    public String inLn() {
        return read.nextLine();
    }
}
