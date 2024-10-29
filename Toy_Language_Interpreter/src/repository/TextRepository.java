package repository;

import exceptions.RepoException;
import model.states.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class TextRepository implements IRepository
{
    protected LinkedList<PrgState> programStates;
    protected int currentIndex;
    protected String fileName;

    public TextRepository(String fileName)
    {
        this.programStates = new LinkedList<>();
        currentIndex = 0;
        this.fileName = fileName;
    }

    @Override
    public PrgState getCurrentProgram()
    {
        return programStates.get(currentIndex);
    }

    @Override
    public void addProgram(PrgState program) {
        programStates.add(program);
        currentIndex++;
    }

    public void logPrgStateExec() throws RepoException {
        try
        {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)));
            writer.println(this.getCurrentProgram());
            writer.close();
        } catch (IOException e) {
            throw new RepoException("Error while writting to the file");
        }

    }


}
