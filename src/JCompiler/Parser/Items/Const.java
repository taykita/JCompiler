package JCompiler.Parser.Items;

import JCompiler.Parser.MainParser;

public class Const<TypeV> {
    public Const(String name, MainParser.T type, TypeV value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    private String name;
    private MainParser.T type;
    private TypeV value;

    public String getName() {
        return name;
    }

    public MainParser.T getType() {
        return type;
    }

    public TypeV getValue() {
        return value;
    }
}
