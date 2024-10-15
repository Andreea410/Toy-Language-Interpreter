package model.statements;
import model.statements.IStmt;

public class CompStmt implements IStmt{
    IStmt first;
    IStmt second;

    @Override
    public String toString()
    {
       return "("+first.toString() + ";" + second.toString()+")";
    }

}
