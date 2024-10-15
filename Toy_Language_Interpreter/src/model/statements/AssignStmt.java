package model.statements;

import model.expressions.Exp;

public class AssignStmt implements IStmt{
    private final String id;
    private final Exp exp;

    public AssignStmt(String id , Exp e)
    {
        this.id = id;
        this.exp = e;
    }

    @Override
    public String toString()
    {
        return id+"="+exp.toString();
    }





}
