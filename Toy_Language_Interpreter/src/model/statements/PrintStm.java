package model.statements;
import model.expressions.Exp;

public class PrintStm implements IStmt
{
    private final Exp exp;

    public PrintStm(Exp exp)
    {
        this.exp = exp;
    }

    @Override
    public String toString()
    {
        return "print(" +exp.toString()+")";
    }

}
