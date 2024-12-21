package controller;
import exceptions.*;
import exceptions.EmptyStackException;
import model.adt.*;
import model.statements.ForkStatement;
import model.statements.IStmt;
import model.states.PrgState;
import model.types.IType;
import model.values.IValue;
import model.values.RefValue;
import model.values.StringValue;
import repository.IRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public class Controller
{
    private final IRepository repository;
    ExecutorService executor;

    public Controller(IRepository repo)
    {
        this.repository = repo;
    }

    public void allStep() throws InterruptedException{
        for (PrgState state: repository.getPrgStatesList()) {
            IMyDictionary<String, IType> typeTable = new MyDictionary<>();

            if(!state.getExeStack().isEmpty()) {
                state.getExeStack().peek().typeCheck(typeTable);
            }
        }

        executor = Executors.newFixedThreadPool(2);
        List<PrgState> programsList = removeCompletedPrgStates(repository.getPrgStatesList());

        if (programsList.isEmpty()) {
            System.out.println("No programs to execute.");
            executor.shutdownNow();
            return;
        }

        programsList.forEach(repository::clearLogFile);

        try {
            while (!programsList.isEmpty()) {
                conservativeGarbageCollector(programsList);
                OneStepForAllPrg(programsList);
                programsList.forEach(System.out::println);
                programsList = removeCompletedPrgStates(repository.getPrgStatesList());
            }
        } catch (ControllerException e) {
            System.out.println("Program finished successfully!");
        }

        executor.shutdownNow();
        repository.setPrgList(programsList);
    }


    public void OneStepForAllPrg(List<PrgState> prgStatess) throws ControllerException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgStates = prgStatess.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());

        List<Callable<PrgState>> callableList = prgStates.stream()
                .filter(p -> !p.getExeStack().isEmpty())
                .map((PrgState p) -> (Callable<PrgState>) (p::executeOneStep))
                .toList();

        List<PrgState> newPrgList;
        try {
            newPrgList = executor.invokeAll(callableList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (ExecutionException | InterruptedException e) {
                            System.out.println("Error executing thread: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

        } catch (InterruptedException e) {
            throw new ControllerException(e.getMessage());
        }

        prgStates.addAll(newPrgList);
        repository.setPrgList(prgStates);
        prgStates.forEach(p -> {
            try {
                repository.logPrgStateExec(p);
            } catch (RepoException e) {
                System.out.println("Error logging program state: " + e.getMessage());
            }
        });
    }

    public void runOneStep() throws EmptyStackException, IOException {
        List<PrgState> programStates = this.repository.getPrgStatesList();
        if (programStates.isEmpty()) {
            throw new EmptyStackException("Execution stack is empty");
        }

        PrgState currentProgramState = programStates.get(0); // Assuming the first program state is the one being executed.
        IMyStack<IStmt> exeStack = currentProgramState.getExeStack();

        if (exeStack.isEmpty()) {
            throw new EmptyStackException("Execution stack is empty");
        }

        IStmt currentStatement = exeStack.pop();
        if (currentStatement instanceof ForkStatement) {
            // Handle ForkStatement: create a new program state
            ForkStatement forkStatement = (ForkStatement) currentStatement;
            PrgState newProgramState =forkStatement.execute(programStates.get(0));

            // Add the new program state to the repository and the current program state to the repository as well
            this.repository.addProgram(newProgramState);

            // Log the program state execution for both the current and the new forked state
            repository.logPrgStateExec(currentProgramState);
            repository.logPrgStateExec(newProgramState);
            currentStatement.execute(programStates.get(0));
        } else {
            // Normal statement execution
            currentStatement.execute(currentProgramState);
            System.out.println(currentProgramState);
            repository.logPrgStateExec(currentProgramState);
        }
    }

    private PrgState createForkedProgramState(PrgState currentProgramState, ForkStatement forkStatement) throws EmptyStackException {
        // Create a new execution stack for the forked program state
        IMyStack<IStmt> newExeStack = new MyStack<>();

        // Copy the entire execution stack from the current program state (excluding the fork statement)
        IMyStack<IStmt> stack = currentProgramState.getExeStack();
        for (IStmt stmt : stack.getStack()) {
            newExeStack.push(stmt);
        }

        // Copy the heap, symbol table, and other elements from the current program state
        IMyDictionary<String, IValue> newSymTable = new MyDictionary<>(currentProgramState.getSymTable().getContent());
        IMyHeap newHeap = new MyHeap(currentProgramState.getHeap().getMap());
        IMyList<String> newOutput = new MyList<>(currentProgramState.getOutput().getList());
        IMyDictionary<StringValue, BufferedReader> newFileTable = new MyDictionary<>(currentProgramState.getFileTable().getContent());

        // Push the fork statement to the new program's execution stack
        newExeStack.push(forkStatement);

        // Create the new program state and return it
        return new PrgState(newExeStack, newSymTable, newOutput, currentProgramState.getExeStack().peek(), newFileTable, newHeap);
    }



    public void addProgram(IStmt statement)
    {
        this.repository.addProgram(new PrgState(statement));
    }

    private Map<Integer, IValue> safeGarbageCollector(IMyList<Integer> symTableAddr, IMyHeap heap) {
        synchronized ( heap) {
            IMyList<Integer> addresses = new MyList<>(symTableAddr.getList());
            boolean newAddressesFound;
            do {
                newAddressesFound = false;
                IMyList<Integer> newAddresses = getAddrFromSymTable(getReferencedValues(addresses, heap));

                for (Integer address : newAddresses.getList()) {
                    if (!addresses.getList().contains(address)) {
                        addresses.add(address);
                        newAddressesFound = true;
                    }
                }
            } while (newAddressesFound);

            Map<Integer, IValue> result = new HashMap<>();
            for (Map.Entry<Integer, IValue> entry : heap.getMap().entrySet()) {
                if (addresses.getList().contains(entry.getKey())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            return result;
        }
    }

    private void conservativeGarbageCollector(List<PrgState> programStates) {
        List<Integer> symTableAddresses = programStates.stream()
                .flatMap(p -> getAddrFromSymTable(p.getSymTable().getContent().values()).getList().stream())
                .collect(Collectors.toList());

        programStates.forEach(p -> {
            Map<Integer, IValue> newHeapContent = safeGarbageCollector(new MyList<>(symTableAddresses), p.getHeap());
            p.getHeap().setContent(newHeapContent);
        });
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

    private List<PrgState> removeCompletedPrgStates(List<PrgState> prgStates) {
        return prgStates.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public List<PrgState> getProgramStateList() {
        return repository.getPrgStatesList();
    }

    public void setProgramStateList(List<PrgState> prgStates) {
        repository.setPrgList(prgStates);
    }

    private void updateHeap()
    {
        PrgState currentProgramState = this.getProgramStateList().get(0);
        currentProgramState.getHeap().setContent(safeGarbageCollector(getAddrFromSymTable(currentProgramState.getSymTable().getContent().values()), currentProgramState.getHeap()));
    }

}
