package model.adt;
import exceptions.KeyNotFoundException;

import java.util.Map;
import java.util.HashMap;

public class MyDictionary<K,V> implements IMyDictionary<K,V>
{
    private final Map<K,V> map;
    private  K key;
    private  V value;



    public MyDictionary()
    {
        this.map = new HashMap<>();
    }

    @Override
    public void insert(K key , V value)
    {
        this.map.put(key , value);
    }

    @Override
    public V getValue(K key) throws KeyNotFoundException
    {
        if(!contains(key))
            throw new KeyNotFoundException("The key doesn t exist");
        for(int i = 0;i < )
    }

    @Override
    public boolean contains(K key)
    {
        return map.containsKey(key);
    }

    @Override
    public void remove(K key) throws KeyNotFoundException
    {
        //TO DO
        return;
    }

}
