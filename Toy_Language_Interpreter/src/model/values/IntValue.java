package model.values;
import model.types.IntType;
import model.types.Type;
import model.values.Value;

public class IntValue implements Value
{
    int val;

    public IntValue(int v)
    {
        this.val =v;
    }

    @Override
    public Type getType()
    {
        return new IntType();
    }

    public int getVal()
    {
        return this.val;
    }

    @Override
    public String toString()
    {
        return "";
    }


}
