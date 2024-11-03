package model.states;
import model.adt.*;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

import java.nio.Buffer;


public class PrgState {
    private IStmt statement;
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
    protected IMyList<String> output;
    protected IStmt originalProgram;

    private IMyDictionary<StringValue, BufferedReader> fileTable;


    public PrgState(IStmt statement)
    {
        this.statement = statement;
        this.exeStack = new MyStack<>();
        this.symTable = new MyDictionary<>();
        this.output = new MyList<>();

        exeStack.push(statement);
    }

    public PrgState(IMyStack<IStmt> e , IMyDictionary<String,IValue> dictionary , IMyList<String> list , IStmt InitialStatement , MyDictionary<StringValue , BufferedReader> dic)
    {
        this.exeStack = e;
        this.symTable = dictionary;
        this.output = list;
        this.fileTable = dic;
        exeStack.push(InitialStatement);
    }

    public String fileTableToString()
    {
        StringBuilder text = new StringBuilder();
        text.append("FileTable: \n");
        for(StringValue key : this.fileTable.getKeys())
            text.append(key);
        return text.toString();
    }

    @Override
    public String toString()
    {
        return symTable.toString() + " " + exeStack.toString() + " " + output.toString() + " " + this.fileTableToString();
    }

    public IMyList<String> getOutput()
    {
        return this.output;
    }


}
