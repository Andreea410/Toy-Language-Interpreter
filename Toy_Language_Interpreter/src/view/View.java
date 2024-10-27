package view;

import controller.Controller;
import model.expressions.ArithmeticalExpression;
import model.expressions.ValueExpression;
import model.expressions.VariableExpression;
import model.statements.*;
import model.types.IntIType;
import model.values.IntIValue;

import javax.management.ValueExp;
import java.util.Scanner;

public class View
{
    private final Controller controller;

    public View(Controller c)
    {
        this.controller = c;
    }


    public void run()
    {
        try
        {

        }

    }

    private void addProgram2()
    {
        IStmt statement = new CompStmt(new VariablesDeclarationStmt("a",new IntIType()),
                new CompStmt(new VariablesDeclarationStmt("b",new IntIType()),
                        new CompStmt(new AssignStmt("a", new ArithmeticalExpression(new ValueExpression(new IntIValue(2)),new
                                ArithmeticalExpression(new ValueExpression(new IntIValue(3)),'*', new ValueExpression(new IntIValue(5)),), '+')),
                                new CompStmt(new AssignStmt("b",new ArithmeticalExpression(new VariableExpression("a"), '+',new ValueExpression(new
                                        IntIValue(1)))), new PrintStm(new VariableExpression("b"))))));
    }

    private void runProgram(IStmt statement)
    {
        this.controller.addProgram(statement);
    }

}
