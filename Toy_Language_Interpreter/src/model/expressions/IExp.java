package model.expressions;
import model.adt.IMyDictionary;
import model.values.IValue;

public interface IExp {
    IValue eval(IMyDictionary<String,IValue> symTable);
}
