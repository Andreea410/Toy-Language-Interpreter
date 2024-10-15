package model.statements;
import model.types.Type;

public class VariablesDeclarationStmt implements IStmt {
    private String name;
    private Type typ;

    public VariablesDeclarationStmt(String n , Type t)
    {
        this.name = n;
        this.typ = t;
    }
}
