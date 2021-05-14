package JCompiler.Parser;

import JCompiler.Exceptions.ParserExceptions.ContextException;
import JCompiler.Parser.Items.Var;

import java.util.*;

public class Table {
    private Stack<HashMap<String, Object>> table = new Stack<>();

    public void openScope() {
        table.add(new HashMap<String, Object>());
    }

    public void closeScope() {
        table.pop();
    }

    private void Add(Object item, String name) {
        HashMap<String, Object> map = table.lastElement();
        map.put(name, item);
    }

    public void set(Object item, String name) {
        HashMap<String, Object> map = table.lastElement();
        if (map.containsKey(name)) {
            throw new ContextException("Повторное объявление имени");
        }
        Add(item, name);
    }

    public Object find(String name) {
        for (HashMap<String, Object> block : table) {
            if (block.containsKey(name)) {
                return block.get(name);
            }
        }
        throw new ContextException("Необъявленное имя");
    }

    public List<Var> getVars() {
        List<Var> vars = new ArrayList<>();
        HashMap<String, Object> lastBlock = table.lastElement();
        for (Object item: lastBlock.values()) {
            if (item instanceof Var) {
                vars.add((Var) item);
            }
        }
        return vars;
    }

}
