package kvdb.oo.v1;

import java.util.Map;

public interface Command {
    String execute(Map<String, String> store);
}
