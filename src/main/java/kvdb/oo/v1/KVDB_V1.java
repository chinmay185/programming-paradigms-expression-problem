package kvdb.oo.v1;

import java.util.HashMap;
import java.util.Map;

public class KVDB_V1 {
    public static void main(String[] args) {

        Map<String, String> store = new HashMap<>();

        String value = new SetCommand("key", "value").execute(store);
        assert value.equals("value");

        value = new GetCommand("key").execute(store);
        assert value.equals("value");

        new DelCommand("key").execute(store);

        value = new GetCommand("key").execute(store);
        assert value == null;
    }
}

class GetCommand implements Command {
    private final String key;
    public GetCommand(String key) {
        this.key = key;
    }
    @Override
    public String execute(Map<String, String> store) {
        return store.get(this.key);
    }
}

class SetCommand implements Command {

    private final String key;
    private final String value;
    public SetCommand(String key, String value) {
        this.key = key;
        this.value = value;
    }
    @Override
    public String execute(Map<String, String> store) {
        store.put(this.key, this.value);
        return this.value;
    }
}

class DelCommand implements Command {
    private final String key;
    public DelCommand(String key) {
        this.key = key;
    }
    @Override
    public String execute(Map<String, String> store) {
        return store.remove(this.key);
    }
}
