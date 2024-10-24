package model.states;
import model.adt.IMyDictionary;
import model.adt.IMyList;
import model.adt.IMyStack;
import model.statements.IStmt;
import model.values.IValue;

public class PrgState {
    protected IMyStack<IStmt> exeStack;

    public IMyStack<IStmt> getExeStack() {
        return exeStack;
    }

    public void setExeStack(IMyStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public IMyDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public void setSymTable(IMyDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    protected IMyDictionary<String , IValue> symTable;
    protected IMyList<IValue> out;
    protected IStmt originalProgram;

    public PrgState(IMyStack<IStmt> e , IMyDictionary<String,IValue> dictionary , IMyList<IValue> list , IStmt prg)
    {
        this.exeStack = e;
        this.symTable = dictionary;
        this.out = list;
        originalProgram = deepCopy(prg);
        e.push(prg);
    }

    private IStmt deepCopy(IStmt prg) {
        return prg;
    }

    @Override
    public String toString()
    {
        return symTable.toString() + " " + exeStack.toString() + " " + out.toString();
    }
}
