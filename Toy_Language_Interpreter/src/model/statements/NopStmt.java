package model.statements;

import model.states.PrgState;

public class NopStmt implements IStmt
{
    @Override
    public PrgState execute(PrgState prgState)
    {
        return prgState;
    }

    @Override
    public IStmt deepCopy() {
        return null;
    }

    @Override
    public String toString()
    {
        return "NopStatements";
    }
}
