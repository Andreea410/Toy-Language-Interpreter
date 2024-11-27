package controller;

import exceptions.*;
import exceptions.EmptyStackException;
import model.adt.IMyHeap;
import model.adt.IMyList;
import model.adt.IMyStack;
import model.adt.MyList;
import model.statements.IStmt;
import model.states.PrgState;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Controller
{
    private final IRepository repository;
    private boolean displayFlag;
    private final ExecutorService executor;

    public Controller(IRepository repo , boolean flag,ExecutorService executor)
    {
        displayFlag =flag;
        this.repository = repo;
        this.executor = executor;
    }

    public PrgState executeOneStep(PrgState prgState) throws EmptyStackException, StatementException, ADTException, IOException {
        IMyStack<IStmt> executionStack = prgState.getExeStack();
        if(executionStack.isEmpty())
            throw new EmptyStackException("The execution stack is empty");

        IStmt currentStatement = executionStack.pop();
        currentStatement.execute(prgState);
        if (displayFlag)
            displayCurrentState(prgState);
        repository.logPrgStateExec(prgState);
        return prgState;
    }

    public void executeAllSteps() throws StatementException, ExpressionException, ADTException, IOException, EmptyStackException {
        PrgState currentProgramState = repository.getCurrentProgram();
        displayCurrentState(currentProgramState);
        repository.logPrgStateExec(currentProgramState);

        while (!currentProgramState.getExeStack().isEmpty()) {
            IMyList<Integer> symTableAddresses = getAddrFromSymTable(currentProgramState.getSymTable().getContent().values());
            Map<Integer, IValue> newHeapContent = unsafeGarbageCollector(symTableAddresses, currentProgramState.getHeap());
            currentProgramState.getHeap().setContent(newHeapContent);
            executeOneStep(currentProgramState);
            repository.logPrgStateExec(currentProgramState);

        }
    }

    public void allStep()
    {

    }

    public void OneStepForAllPrg(List<PrgState> prgStates) throws ControllerException, InterruptedException {

        prgStates.forEach(prgState -> repository.logPrgStateExec());
         List<Callable<PrgState>> callList = prgStates.stream().map((PrgState p)-> (Callable<PrgState>)(() -> {return p.oneStep();})).toList();

         List<PrgState> newPrgStates = executor.invokeAll(callList).stream().map(future->{
             try
             {
                 future.get();
             } catch (InterruptedException | ExecutionException e) {
                 throw new ControllerException(e.getMessage());
             }
         }).filter(Objects::nonNull).toList();
         prgStates.addAll(newPrgStates);
         repository.setPrgList(prgStates);
         prgStates.forEach(prg);

    }

    public void displayCurrentState(PrgState prgState) {
        System.out.println(prgState.toString() + "\n");
    }

    public void addProgram(IStmt statement)
    {
        this.repository.addProgram(new PrgState(statement));
    }

    public IRepository getRepository()
    {
        return  this.repository;
    }

    private Map<Integer, IValue> unsafeGarbageCollector(IMyList<Integer> symTableAddr, IMyHeap heap)
    {
        IMyList<Integer> addresses = new MyList<>(symTableAddr.getList());
        boolean newAddressesFound;
        do {
            newAddressesFound = false;
            IMyList<Integer> newAddresses = getAddrFromSymTable(getReferencedValues(addresses,heap));

            for(Integer address: newAddresses.getList())
                if(!addresses.getList().contains(address))
                {
                    addresses.add(address);
                    newAddressesFound = true;
                }
        }while (newAddressesFound);

        Map<Integer, IValue> result = new HashMap<>();
        for (Map.Entry<Integer, IValue> entry : heap.getMap().entrySet()) {
            if (addresses.getList().contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }



    private List<IValue> getReferencedValues(IMyList<Integer> addresses, IMyHeap heap) {
        List<IValue> referencedValues = new ArrayList<>();
        for (Integer address : addresses.getList()) {
            IValue value = heap.getValue(address);
            if (value != null) {
                referencedValues.add(value);
            }
        }
        return referencedValues;
    }

    private IMyList<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        IMyList<Integer> addressList = new MyList<>();
        for (IValue value : symTableValues) {
            if (value instanceof RefValue) {
                addressList.add(((RefValue) value).getAddress());
            }
        }
        return addressList;
    }

    private List<PrgState> removeCompletedPrgStates(List<PrgState> prgStates)
    {
        return prgStates.stream().filter(PrgState::isComplete).collect(Collectors.toList());
    }



}
