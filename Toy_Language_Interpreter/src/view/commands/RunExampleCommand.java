package view.commands;

import controller.Controller;
import exceptions.EmptyStackException;

import java.io.IOException;

public class RunExampleCommand extends Command {

    private final Controller controller;

    public RunExampleCommand(String key , String description , Controller controller)
    {
        super(key , description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.allStep();
        }
        catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
