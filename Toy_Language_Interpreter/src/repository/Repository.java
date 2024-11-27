package repository;

import exceptions.RepoException;
import model.statements.PrintStm;
import model.states.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class Repository implements IRepository
{
    private List<PrgState> programs;
    private final String filename;

    public Repository(String file)
    {
        this.programs = new LinkedList<>();
        this.filename = file;
    }

    @Override
    public List<PrgState> getStates()
    {
        return this.programs;
    }

    @Override
    public void addProgram(PrgState program)
    {
        this.programs.add(program);
    }

    @Override
    public void logPrgStateExec(PrgState prgState) throws RepoException
    {
        try
        {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename , true)));
            writer.println(prgState.toString());
            writer.close();
        } catch (IOException e) {
            throw new RepoException("File doesn't exit");
        }
    }

    @Override
    public List<PrgState> getPrgStatesList() {
        return this.programs;
    }

    @Override
    public void setPrgList(List<PrgState> programStates) {
        this.programs = programStates;
    }

}
