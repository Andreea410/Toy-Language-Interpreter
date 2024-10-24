package model.statements;
import model.adt.IMyDictionary;
import model.types.IType;
import model.values.IValue;

public class VariablesDeclarationStmt implements IStmt {
    private String name;
    private IType typ;

    public VariablesDeclarationStmt(String n , IType t)
    {
        this.name = n;
        this.typ = t;
    }

    @Override
    public IValue eval(IMyDictionary<String,IValue> symTable)
    {
        return symTable.getValue(name);
    }

    public String toString()
    {
        return name;
    }



}
