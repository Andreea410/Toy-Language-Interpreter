package model.adt;

import model.values.IValue;

public interface IMyHeap
{
    public int allocate(IValue value);
    public IValue getValue(int key);
    public void set(int key , IValue value);
    IMyMap<Integer , IValue> getMap();
    public boolean containsKey(int key);
}
