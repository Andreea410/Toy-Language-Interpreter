package model.adt;
import java.util.ArrayList;
import java.util.List;

public interface IMyList<T>
{
    void add(T element);
    List<T> getAll();

}
