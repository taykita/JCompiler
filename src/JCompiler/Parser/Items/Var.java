package JCompiler.Parser.Items;

import JCompiler.Parser.MainParser;

public class Var<Type> {
    public Var(String name, MainParser.T type) {
        this.name = name;
        this.type = type;
    }

    private String name;
    private MainParser.T type;
    private int addr = 0;

    public String getName() {
        return name;
    }

    public MainParser.T getType() {
        return type;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }
}