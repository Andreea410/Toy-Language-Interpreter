package controller;

import exceptions.*;
import model.adt.IMyHeap;
import model.adt.IMyList;
import model.adt.IMyStack;
import model.statements.IStmt;
import model.states.PrgState;
import model.values.IValue;
import repository.IRepository;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Controller
{
    private final IRepository repository;
    private boolean displayFlag;

    public Controller(IRepository repo , boolean flag)
    {
        displayFlag =flag;
        this.repository = repo;
    }

    public PrgState executeOneStep(PrgState prgState) throws EmptyStackException, StatementException, ADTException, IOException {
        IMyStack<IStmt> executionStack = prgState.getExeStack();
        if(executionStack.isEmpty())
            throw new EmptyStackException("The execution stack is empty");

        IStmt currentStatement = executionStack.pop();
        currentStatement.execute(prgState);
        if (displayFlag)
            displayCurrentState(prgState);
        repository.logPrgStateExec();
        return prgState;
    }

    public void executeAllSteps() throws StatementException, ExpressionException, ADTException, IOException, EmptyStackException {
        PrgState currentProgramState = repository.getCurrentProgram();
        displayCurrentState(currentProgramState);
        repository.logPrgStateExec();

        while (!currentProgramState.getExeStack().isEmpty()) {
                executeOneStep(currentProgramState);
        }
    }

    public void displayCurrentState(PrgState prgState) {
        System.out.println(prgState.toString() + "\n");
    }

    public void addProgram(IStmt statement)
    {
        this.repository.addProgram(new PrgState(statement));
    }

    public IRepository getRepository()
    {
        return  this.repository;
    }

    private Map<Integer, IValue> unsafeGarbageCollector(IMyList<Integer> symTableAddr, IMyHeap heap)
    {
        return heap.getMap()
    }
}
