package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.adt.IMyDictionary;
import model.adt.IMyStack;
import model.expressions.LogicalExpression;
import model.expressions.LogicalOperator;
import model.states.PrgState;
import model.types.BoolIType;
import model.types.IType;
import model.expressions.IExp;
import java.io.IOException;

public class RepeatUntilStatement implements IStmt{

    private final IStmt statement;
    private final IExp condition;

    public RepeatUntilStatement( IStmt stmt,IExp cond) {
        this.statement = stmt;
        this.condition = cond;
    }

    @Override
    public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {
        IMyStack<IStmt> stack = prgState.getExeStack();
        IStmt whileStatement = new CompStmt(statement, new WhileStatement(new LogicalExpression(condition, LogicalOperator.NOT, condition), statement));

        stack.push(whileStatement);
        prgState.setExeStack(stack);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new RepeatUntilStatement(statement.deepCopy() , condition.deepCopy());
    }

    @Override
    public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {

       IType type = condition.typecheck(typeEnv);
        if(!type.equals(new BoolIType()))
            throw new StatementException("The condition of RepeatUntil is not a boolean");
        statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    public String toString()
    {
        return String.format("repeat" + "(" + this.statement + "until" + this.condition + ")");
    }
}



