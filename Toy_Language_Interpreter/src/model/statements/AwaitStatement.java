package model.statements;

import exceptions.StatementException;
import javafx.util.Pair;
import model.adt.IMyDictionary;
import model.adt.IMyBarrierTable;
import model.adt.MyPair;
import model.statements.IStmt;
import model.states.PrgState;
import model.types.IType;
import model.types.IntIType;
import model.values.IValue;
import model.values.IntIValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitStatement implements IStmt {
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public AwaitStatement(String var) {
        this.variable = var;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        lock.lock();

        IMyDictionary<String, IValue> symTable = state.getSymTable();
        IMyBarrierTable barrierTable = state.getBarrierTable();

        if (symTable.contains(variable)) {

            IntIValue f = (IntIValue) symTable.getValue(variable);
            int foundIndex = f.getVal();

            if (barrierTable.containsKey(foundIndex)) {

                MyPair<Integer, List<Integer>> currentBarriers = barrierTable.get(foundIndex);
                ArrayList<Integer> list = (ArrayList<Integer>) currentBarriers.getSecond();

                int length = currentBarriers.getSecond().size();
                int currentKey = currentBarriers.getFirst();

                if (currentKey > length) {

                    if (list.contains(state.getId()))
                        state.getExeStack().push(this);
                    else {

                        list.add(state.getId());
                        barrierTable.update(foundIndex, new MyPair<>(currentKey, list));
                        state.setBarrierTable(barrierTable);

                    }
                }
            } else {
                throw new StatementException("Await: Address not in Barrier Table");
            }
        } else {
            throw new StatementException("Await: Variable not in symbol table");
        }
        lock.unlock();
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {
        if (typeEnv.getValue(variable).equals(new IntIType()))
            return typeEnv;
        else
            throw new StatementException("Var is not of type int!");
    }

    @Override
    public IStmt deepCopy() {
        return new AwaitStatement(variable);
    }

    @Override
    public String toString() {
        return String.format("barrierAwait(%s)", variable);
    }
}