//Compund statement

package model.statements;
import exceptions.StatementException;
import model.statements.IStmt;
import model.states.PrgState;
import model.values.IValue;

import javax.management.ValueExp;

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

    @Override
    public PrgState execute(PrgState prgState) throws StatementException
    {
        prgState.getExeStack().push(second);
        prgState.getExeStack().push(first);
    }

    public ValueExp(IValue value)
    {

    }





}
