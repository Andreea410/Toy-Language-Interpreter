package model.adt;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import exceptions.EmptyStackException;

public class MyStack<T> implements IMyStack<T>{

    private final Stack<T> stack;

    public MyStack()
    {
        this.stack = new Stack<>();
    }

    @Override
    public T pop() throws EmptyStackException{
        if(stack.isEmpty())
            throw new EmptyStackException("Stack is empty");
        T element;
        element = stack.pop();
        return element;
    }

    @Override
    public void push(T v)
    {
        stack.push(v);
    }

    @Override
    public int getSize()
    {
        return stack.size();
    }

    @Override
    public Stack<T> getStack()
    {
        return this.stack;
    }
}
