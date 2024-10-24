package model.statements;
import exceptions.StatementException;
import model.expressions.IExp;
import model.types.BoolIType;
import model.values.IValue;
import model.states.PrgState;


public class IfStmt implements IStmt{
    private final IExp exp;
    private final IStmt thenS;
    private final IStmt elseS;

    public IfStmt(IExp e , IStmt t , IStmt el)
    {
        this.exp = e;
        this.thenS = t;
        this.elseS = el;
    }

    @Override
    public String toString()
    {
        return "(IF(" +exp.toString()+ "){THEN("+thenS.toString()+")ELSE("+elseS.toString()+"))";
    }

    public PrgState execute(PrgState state)
    {
        IValue value = exp.eval(state.getSymTable());
        if(!value.getType().equals(new BoolIType()))
            throw new StatementException("Expression is not boolean");

    }

}
