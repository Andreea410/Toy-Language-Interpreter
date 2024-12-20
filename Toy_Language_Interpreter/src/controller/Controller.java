package controller;
import exceptions.*;
import exceptions.EmptyStackException;
import model.adt.*;
import model.statements.IStmt;
import model.states.PrgState;
import model.types.IType;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepository;

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
        List<PrgState> prgStates = removeCompletedPrgStates(prgStatess);

        if (prgStates.isEmpty()) {
            throw new ControllerException("No more programs to execute. Execution is complete.");
        }

        prgStates.forEach(prgState -> {
            try {
                repository.logPrgStateExec(prgState);
            } catch (RepoException e) {
                throw new RepoException(e.getMessage());
            }
        });

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

        for (PrgState newState : newPrgList) {
            if (!prgStates.contains(newState)) {
                prgStates.add(newState);
            }
        }


        prgStates.forEach(prgState -> {
            try {
                repository.logPrgStateExec(prgState);
            } catch (RepoException e) {
                throw new ControllerException("An error occurred executing one step: " + e);
            }
        });

        repository.setPrgList(prgStates);
    }

    public void runOneStep() throws EmptyStackException, IOException {
        IMyStack<IStmt> exeStack = repository.getPrgStatesList().get(0).getExeStack();
        if(exeStack.isEmpty())
            throw new EmptyStackException("Execution stack is empty.");
        IStmt currentStatement = exeStack.pop();
        currentStatement.execute(repository.getPrgStatesList().get(0));

        System.out.println(repository.getPrgStatesList().get(0).toString());
        repository.logPrgStateExec(repository.getPrgStatesList().get(0));

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

}
