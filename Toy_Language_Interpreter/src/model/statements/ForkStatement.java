package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.states.PrgState;

import java.io.IOException;

public class ForkStatement implements IStmt
{
    private IStmt statement;

    public ForkStatement(IStmt statement)
    {
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {
        return new PrgState(this.statement, prgState.getSymTable(), prgState.getOutput(),prgState.getFileTable(),prgState.getHeap(),);
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStatement(this.statement.deepCopy());
    }

    @Override
    public String toString()
    {
        return "Fork("+ statement.toString()+")";
    }
}
