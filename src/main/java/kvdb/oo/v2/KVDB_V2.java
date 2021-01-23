package kvdb.oo.v2;

import kvdb.oo.v1.Command;

import java.util.HashMap;
import java.util.Map;

public class KVDB_V2 {
    public static void main(String[] args) {

        Map<String, String> store = new HashMap<>();

        String value = new IncrCommand("counter").execute(store);
        assert value.equals("1");
        value = new IncrCommand("counter").execute(store);
        assert value.equals("2");
    }
}

class IncrCommand implements Command {

    private final String key;
    public IncrCommand(String key) {
        this.key = key;
    }
    @Override
    public String execute(Map<String, String> store) {
        String current = store.get(key);
        String incremented = "1";
        if (current == null) {
            store.put(this.key, incremented);
        } else {
            incremented = (Integer.parseInt(current) + 1) + "";
            store.put(this.key, incremented);
        }
        return incremented;
    }
}
