package model.statements;
import model.statements.IStmt;

public class CompStmt implements IStmt{
    private final IStmt first;
    private final IStmt second;

    public CompStmt(IStmt f , IStmt s)
    {
        first = f;
        second = s;
    }

    @Override
    public String toString()
    {
       return "("+first.toString() + ";" + second.toString()+")";
    }

}
