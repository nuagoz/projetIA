package Model;

import java.util.HashMap;

public class Hashtable {

    HashMap<Long, TableItem> _table;

    public Hashtable() {
        _table = new HashMap<>();
    }

    public void add(long hash, TableItem item) {
        _table.put(hash, item);
    }

    public boolean isExist(long hash) {
        return _table.containsKey(hash);
    }

    public TableItem get(long hash) {
        return _table.get(hash);
    }

    public void clear() {
        _table.clear();
    }
}
