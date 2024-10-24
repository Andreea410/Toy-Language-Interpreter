package model.types;

public class BoolIType implements IType {


    @Override
    public boolean equals(IType obj) {
        return obj instanceof BoolIType;
    }

    @Override
    public String toString()
    {
        return "bool";
    }
}
