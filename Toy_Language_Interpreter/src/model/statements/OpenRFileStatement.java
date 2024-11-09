package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.adt.MyDictionary;
import model.expressions.IExp;
import model.states.PrgState;
import model.types.StringType;

import java.beans.Expression;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStatement implements IStmt {

    private final IExp expression;
    private final String filename;

    OpenRFileStatement(IExp exp , String value)
    {
        this.expression = exp;
        this.filename = value;
    }

    @Override
    public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {
        var table = prgState.getSymTable();

        var res = expression.eval(table);
        if(!res.getType().equals(new StringType()))
            throw new StatementException("It must be of String type");
        if(table.contains(filename))
            throw new ADTException("It results in a duplicate value");

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.readLine();


        }
        catch(IOException e)
        {
            throw new StatementException(e.toString());
        }
    }

    @Override
    public IStmt deepCopy()
    {
        return new OpenRFileStatement(expression);
    }

    public IStmt pop()
    {
        return null;
    }
}
