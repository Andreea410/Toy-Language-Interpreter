package model.statements;

import model.expressions.IExp;
import model.states.PrgState;

public class AssignStmt implements IStmt{
    private final String id;
    private final IExp exp;

    public AssignStmt(String id , IExp e)
    {
        this.id = id;
        this.exp = e;
    }

    @Override
    public String toString()
    {
        return id+"="+exp.toString();
    }

    @Override
    public PrgState execute(PrgState prgState)
    {
        if(prgState.getSymTable().contains())
    }





}
