package controller;

import exceptions.ADTException;
import exceptions.EmptyStackException;
import exceptions.RepoException;
import exceptions.StatementException;
import model.adt.IMyStack;
import model.statements.IStmt;
import model.states.PrgState;
import repository.IRepository;

import java.io.IOException;

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
        return prgState;
    }

    public void executeAllSteps() throws EmptyStackException, StatementException, ADTException, IOException, RepoException {
        PrgState currentPrgState = this.repository.getCurrentProgram();
        repository.logPrgStateExec();
        while(!currentPrgState.getExeStack().isEmpty())
        {
            executeOneStep(currentPrgState);
            repository.logPrgStateExec();
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
}
