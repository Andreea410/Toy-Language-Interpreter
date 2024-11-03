package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.expressions.IExp;
import model.states.PrgState;
import model.types.IntIType;
import model.types.StringType;
import model.values.IntIValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStmt
{
    private IExp expressiom;
    private String variableName;

    public ReadFileStatement(IExp ex , String variableName)
    {
        this.expressiom = ex;
        this.variableName = variableName;
    }

    @Override
    public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {
        var table = prgState.getSymTable();

        if(!table.contains(variableName))
        {
            throw new StatementException("The variable was not defined earlier");
        }

        if(!table.getValue(variableName).getType().equals(new IntIType()))
        {
            throw new StatementException("The type is incorrect");
        }

        var res = expressiom.eval(table);

        if(!res.getType().equals(new StringType()))
        {
            throw new StatementException("The result is not a String type");
        }

        BufferedReader reader = prgState.getFileTable().getValue((StringValue) res);
        String read = reader.readLine();

        if(read.isEmpty())
            read = "0";

        int parser = Integer.parseInt(read);
        table.insert(variableName,new IntIValue(parser));
        return prgState;

    }

    @Override
    public IStmt deepCopy() {
        return null;
    }
}
