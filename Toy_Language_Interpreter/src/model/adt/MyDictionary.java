package model.adt;


public class MyDictionary<T,U> implements IMyDictionary<T,U>
{
    private final T first;
    private final U second;


    public MyDictionary(T f , U s)
    {
        first = f;
        second = s;
    }






}
