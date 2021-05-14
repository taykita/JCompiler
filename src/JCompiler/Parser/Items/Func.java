package JCompiler.Parser.Items;

import JCompiler.Parser.MainParser;

public class Func {
    public Func(String name, MainParser.T type) {
        this.name = name;
        this.type = type;
    }

    private String name;
    private MainParser.T type;

    public String getName() {
        return name;
    }

    public MainParser.T getType() {
        return type;
    }
}
