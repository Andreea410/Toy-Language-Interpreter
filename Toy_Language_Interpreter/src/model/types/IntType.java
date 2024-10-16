package model.types;

public class IntType implements Type {

    @Override
    public boolean equals(Object another)
    {
        return another instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }
}
