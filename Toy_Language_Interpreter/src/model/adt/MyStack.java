package model.adt;
import java.util.ArrayDeque;
import java.util.Deque;

public class MyStack<T> implements IMyStack<T>{

    private final Deque<T> stack;

    public MyStack()
    {
        this.stack = new ArrayDeque<>();
    }

    @Override
    public T pop(){
        if(stack.isEmpty())
            throw new IllegalStateException("Stack is empty");
        T element;
        element = stack.pop();
        return element;
    }

    @Override
    public void push(T v)
    {
        stack.push(v);
    }

    public int getSize()
    {
        return stack.size();
    }
}
