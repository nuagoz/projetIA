package Model;

public class TableItem {
    public int value;
    public int depth;
    public String bestMove;
    public ValueType valueType;

    public TableItem(int value, int depth, String bestMove, ValueType valueType) {
        this.value = value;
        this.depth = depth;
        this.bestMove = bestMove;
        this.valueType = valueType;
    }
}
