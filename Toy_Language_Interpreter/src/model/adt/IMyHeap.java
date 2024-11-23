package model.adt;
import model.values.IValue;
import java.util.Map;

public interface IMyHeap
{
    Integer getFreeValue();
    public void update(Integer position , IValue value);
    public IValue getValue(int key);
    public void set(int key , IValue value);
    Map<Integer , IValue> getMap();
    public boolean containsKey(int key);
}
