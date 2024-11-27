package model.states;
import exceptions.ADTException;
import exceptions.EmptyStackException;
import exceptions.StatementException;
import model.adt.*;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

import java.io.IOException;
import java.nio.Buffer;
import java.util.Map;


public class PrgState {
    private final int id;
    private static int lastIndex;
    protected IMyStack<IStmt> exeStack;
    protected IMyDictionary<String , IValue> symTable;
    protected IMyList<String> output;
    protected IStmt originalProgram;
    private final IMyDictionary<StringValue, BufferedReader> fileTable;
    private IMyHeap heap;

    public IMyStack<IStmt> getExeStack() {
        return exeStack;
    }

    private synchronized int getNewId()
    {
        lastIndex++;
        return lastIndex;
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

    public PrgState(IStmt statement)
    {
        this.exeStack = new MyStack<>();
        this.symTable = new MyDictionary<>();
        this.output = new MyList<>();
        this.fileTable = new MyDictionary<>();
        this.heap = new MyHeap();
        exeStack.push(statement);
        this.id = getNewId();
    }

    public PrgState(IMyStack<IStmt> e , IMyDictionary<String,IValue> dictionary , IMyList<String> list , IStmt InitialStatement , IMyDictionary<StringValue , BufferedReader> fileTable , IMyHeap heap,int id)
    {
        this.exeStack = e;
        this.symTable = dictionary;
        this.output = list;
        this.fileTable = fileTable;
        this.heap = heap;
        exeStack.push(InitialStatement);
        this.id = id;
    }

    public IMyDictionary<StringValue,BufferedReader> getFileTable()
    {
        return this.fileTable;
    }

    public String fileTableToString()
    {
        StringBuilder text = new StringBuilder();
        for(StringValue key : this.fileTable.getKeys())
            text.append(key).append("\n");
        return text.toString();
    }

    public String symTableToString() {
        StringBuilder symbolTableStringBuilder = new StringBuilder();

        for (String key : symTable.getKeys()) {
            symbolTableStringBuilder.append(String.format("%s -> %s\n", key, symTable.getValue(key).toString()));
        }

        return symbolTableStringBuilder.toString();
    }

    public IMyHeap getHeap()
    {
        return this.heap;
    }

    public void setHeap(IMyHeap heap)
    {
        this.heap = heap;
    }

    public String HeapToString()
    {
        StringBuilder answer = new StringBuilder();
            for(Integer key: heap.getMap().keySet()){
                answer.append(key).append("(").append(heap.getValue(key).getType().toString())
                        .append(")").append(":-> ").
                        append(heap.getValue(key).toString()).append("\n");
            }
        return answer.toString();
    }

    @Override
    public String toString() {
        return String.format("EXE_STACK\n%s\nSYM_TABLE\n%s\nOUT\n%s\nFILE_TABLE\n%s\nHEAP\n%S\n", exeStack.toString(), symTableToString(), output.toString(), fileTableToString(),HeapToString());
    }

    public IMyList<String> getOutput()
    {
        return this.output;
    }

    public boolean isNotComplete()
    {
        return ! this.exeStack.isEmpty();
    }

    public PrgState executeOneStep() throws EmptyStackException
    {
        if(exeStack.isEmpty())
            throw new EmptyStackException("Execution Stack Error: Execution stack is empty");

        IStmt currentStatement = exeStack.pop();
        return currentStatement.execute(this);

    }


}
