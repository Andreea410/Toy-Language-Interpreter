package model.statements;
import model.expressions.Exp;


public class IfStmt implements IStmt{
    private final Exp exp;
    private final IStmt thenS;
    private final IStmt elseS;

    public IfStmt(Exp e , IStmt t , IStmt el)
    {
        this.exp = e;
        this.thenS = t;
        this.elseS = el;
    }

    @Override
    public String toString()
    {
        return "(IF("+exp.toString()+")THEN("+thenS.toString()+")ELSE("+elseS.toString()+"))";
    }


}
