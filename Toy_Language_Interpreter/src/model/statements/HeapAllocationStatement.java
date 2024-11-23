package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.expressions.IExp;
import model.states.PrgState;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

import java.io.IOException;

public class HeapAllocationStatement implements IStmt
{

    private final IExp expression;
    private final String var;

    public HeapAllocationStatement(IExp expression , String var)
    {
        this.expression = expression;
        this.var = var ;
    }


    @Override
    public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {

        if(!prgState.getSymTable().contains(var))
            throw new StatementException("Variable was not found");

        IValue variableValue = prgState.getSymTable().getValue(var);
        if (prgState.getSymTable().getValue(var) instanceof RefType)
            throw new StatementException("Variables must be RefType");

        IValue value = expression.eval(prgState.getSymTable(), prgState.getHeap());
        if(!variableValue.getType().equals(value.getType()))
            throw new StatementException("Wrong type for expression");

        int address = prgState.getHeap().allocate(value);
        prgState.getSymTable().insert(var ,new RefValue(address,value.getType()));
        return prgState;
    }

    @Override
    public IStmt deepCopy() {
        return new HeapAllocationStatement(this.expression.deepCopy() , this.var);
    }
}
