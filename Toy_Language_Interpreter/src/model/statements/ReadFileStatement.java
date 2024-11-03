package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.expressions.IExp;
import model.states.PrgState;
import model.types.IntIType;
import model.types.StringType;

import java.io.BufferedReader;

public class ReadFileStatement implements IStmt
{
    private IExp exp;
    private String varName;

    public ReadFileStatement(IExp ex , String varName)
    {
        this.exp = ex;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState prgState) throws StatementException, ADTException {
        var table = prgState.getSymTable();

        if(!table.contains(varName))
        {
            throw new StatementException("The variable was not defined earlier");
        }

        if(!table.getValue(varName).getType().equals(new IntIType()))
        {
            throw new StatementException("The type is incorrect");
        }

        var res = exp.eval(table);

        if(!res.getType().equals(new StringType()))
        {
            throw new StatementException("The statement is not a String type");
        }

        BufferedReader reader = prgState.get
    }

    @Override
    public IStmt deepCopy() {
        return null;
    }
}
