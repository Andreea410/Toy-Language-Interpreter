package model.values;
import model.types.BoolType;
import model.values.Value;
import model.types.Type;

public class BoolValue implements Value
{
    boolean val;

    public BoolValue(boolean v)
    {
        this.val = v;
    }

    @Override
    public String toString()
    {
        return "";
    }

    @Override
    public Type getType()
    {
        return new BoolType();
    }

}
