package model.adt;

import model.values.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyHeap implements IMyHeap{
    private final Map<Integer,IValue> map;

    public MyHeap(Map<Integer,IValue> map)
    {
        this.map = map;
    }

    public MyHeap()
    {
         this.map = new HashMap<>();
    }

    @Override
    public int allocate(IValue value) {
        return 0;
    }

    @Override
    public IValue getValue(int key) {
        return map.get(key);
    }

    @Override
    public void set(int key, IValue value) {
        map.put(key,value);
    }

    @Override
    public Map<Integer, IValue> getMap() {
        return map;
    }

    @Override
    public boolean containsKey(int key) {
        return map.containsKey(key);
    }
}
