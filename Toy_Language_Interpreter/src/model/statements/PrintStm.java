package model.statements;
import model.expressions.IExp;
import model.states.PrgState;
import model.values.IValue;

public class PrintStm implements IStmt
{
    private final IExp exp;

    public PrintStm(IExp exp)
    {
        this.exp = exp;
    }

    @Override
    public String toString()
    {
        return "print(" +exp.toString()+")";
    }

    public PrgState execute(PrgState prgState)
    {
        IValue result =  exp.eval(prgState.getSymTable());
        prgState.getOutput().add(result.toString());
        return prgState;
    }

}
