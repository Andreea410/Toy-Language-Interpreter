package model.types;

public class IntIType implements IType {

    @Override
    public boolean equals(IType another)
    {
        return another instanceof IntIType;
    }

    @Override
    public String toString() {
        return "int";
    }
}
