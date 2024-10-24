package model.expressions;


import model.adt.MyDictionary;
import model.types.BoolIType;
import model.values.BoolValue;
import model.values.IValue;

public class LogicalExpression implements IExp{
    private IExp left;
    private IExp right;
    private LogicalOperator operator;

    public LogicalExpression(IExp l, LogicalOpeartor opeartor, IExp r)
    {
        this.left =l;
        this.operator = opeartor;
        this.right = r;
    }

    @Override
    public IValue eval(MyDictionary<String,IValue> symtbl) throws ADTExcetpion , ExpressionException
    {
       IValue evaluatedExpressionLeft = left.eval(symtbl);
       IValue evaluatedExpressionRight = right.eval(symtbl);
       if(!evaluatedExpressionLeft.getType().equals(new BoolIType()))
       {
           throw new ExpressionException("Left expression is not of type BoolType");
       }
       if(!evaluatedExpressionRight.getType().equals(new BoolIType()))
       {
           throw new ExpressionException("Right expression is not of type BoolType");
       }
       switch(operator)
       {
           case AND:
               return new BoolValue()
       }


    }

}
