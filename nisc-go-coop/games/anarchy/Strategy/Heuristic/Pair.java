package games.anarchy.Strategy.Heuristic;

/**
 * Created by squeaky on 11/14/15.
 */
public class Pair<T, T1> {
    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public T1 getValue() {
        return value;
    }

    public void setValue(T1 value) {
        this.value = value;
    }

    private T key;
    private T1 value;
    public Pair(T elementOne, T1 elementTwo) {
        key = elementOne;
        value = elementTwo;
    }
}
