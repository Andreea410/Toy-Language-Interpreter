package view.gui.executewindow;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import model.adt.IMyHeap;
import model.adt.MyPair;
import model.states.PrgState;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ExecuteStatementController {

    private Controller controller;

    @FXML
    private TextField numberProgramStatesTextField;

    @FXML
    private TableView<String> heapTableView;
    @FXML
    private TableColumn<String, String> addressColumn;
    @FXML
    private TableColumn<String, String> valueColumn;

    @FXML
    private Label heapLabel;

    @FXML
    private ListView<String> outputListView;
    @FXML
    private Label outputLabel;

    @FXML
    private ListView<String> fileTableListView;
    @FXML
    private Label fileTableLabel;

    @FXML
    private ListView<String> identifiersListView;
    @FXML
    private Label identifiersLabel;

    @FXML
    private TableView<String> symTableView;
    @FXML
    private TableColumn<String, String> variableNameColumn;
    @FXML
    private TableColumn<String, String> valueColumnSymTable;

    @FXML
    private ListView<String> executionStackListView;
    @FXML
    private Label executionStackLabel;

    @FXML
    private Button runOneStepButton;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public PrgState getCurrentProgramState() {
        if(controller.getProgramStateList().size() == 0)
            return null;
        else {
            int id = identifiersListView.getSelectionModel().getSelectedIndex();
            if(id == -1)
                return controller.getProgramStateList().get(0);
            else
                return controller.getProgramStateList().get(id);
        }
    }

    //Functions to populate the tables
    private void populateHeapTable()
    {
        PrgState currentProgramState = getCurrentProgramState();
        IMyHeap heap = Objects.requireNonNull(currentProgramState).getHeap();
        ArrayList<MyPair<Integer, IValue>> heapContent =new ArrayList<>();
        for(Integer key: heap.getMap().keySet())
            heapContent.add(new MyPair<>(key,heap.getMap().get(key)));
        heapTableView.getItems().clear();
        for(MyPair<Integer,IValue> entry: heapContent)
        {
            heapTableView.getItems().add(entry.getFirst().toString());
            heapTableView.getItems().add(entry.getSecond().toString());
        }
    }

    private void populateSymTable()
    {
        PrgState currentProgramState = getCurrentProgramState();
        var symTable = Objects.requireNonNull(currentProgramState).getSymTable();
        ArrayList<MyPair<String, IValue>> symTableContent = new ArrayList<>();
        for(String key: symTable.getContent().keySet())
            symTableContent.add(new MyPair<>(key,symTable.getContent().get(key)));
        symTableView.getItems().clear();
        for(MyPair<String,IValue> entry: symTableContent)
        {
            symTableView.getItems().add(entry.getFirst());
            symTableView.getItems().add(entry.getSecond().toString());
        }
    }

    private void populateExecutionStack()
    {
        PrgState currentProgramState = getCurrentProgramState();
        var exeStack = Objects.requireNonNull(currentProgramState).getExeStack();
        ArrayList<String> exeStackContent = new ArrayList<>();
        for(var stmt: exeStack.getStack())
            exeStackContent.add(stmt.toString());
        executionStackListView.getItems().clear();
        for(String entry: exeStackContent)
            executionStackListView.getItems().add(entry);
    }

    private void populateOutput()
    {
        PrgState currentProgramState = getCurrentProgramState();
        var output = Objects.requireNonNull(currentProgramState).getOutput();
        ArrayList<String> outputContent = new ArrayList<>();
        for(String entry: output.getList())
            outputContent.add(entry);
        outputListView.getItems().clear();
        for(String entry: outputContent)
            outputListView.getItems().add(entry);
    }

    private void populateFileTable()
    {
        PrgState currentProgramState = getCurrentProgramState();
        var fileTable = Objects.requireNonNull(currentProgramState).getFileTable();
        ArrayList<MyPair<StringValue, BufferedReader>> fileTableContent = new ArrayList<>();
        for(StringValue key: fileTable.getContent().keySet())
            fileTableContent.add(new MyPair<>(key,fileTable.getContent().get(key)));
        fileTableListView.getItems().clear();
        for(MyPair<StringValue,BufferedReader> entry: fileTableContent)
        {
            fileTableListView.getItems().add(entry.getFirst().toString());
            fileTableListView.getItems().add(entry.getSecond().toString());
        }
    }

    private void populateIdentifiers()
    {
        List<PrgState> programStates = controller.getProgramStateList();
        List<Integer> identifiers = programStates.stream().map(PrgState::getId).toList();
        identifiersListView.getItems().clear();
        for(Integer id: identifiers)
            identifiersListView.getItems().add(id.toString());
    }

    private void populateNumberProgramStates()
    {
        numberProgramStatesTextField.setText(String.valueOf(controller.getProgramStateList().size()));
    }

    private void runOneStep() {
        updateUI();
    }

    private void updateUI() {

        numberProgramStatesTextField.setText("Updated number");

        heapTableView.getItems().clear();
        heapTableView.getItems().add("Updated Heap entry");

        outputListView.getItems().clear();
        outputListView.getItems().add("Output text");

        fileTableListView.getItems().clear();
        fileTableListView.getItems().add("File Table entry");

        identifiersListView.getItems().clear();
        identifiersListView.getItems().add("Identifier");

        symTableView.getItems().clear();
        symTableView.getItems().add("Variable Name = Value");

        executionStackListView.getItems().clear();
        executionStackListView.getItems().add("Execution Stack item");
    }


    @FXML
    public void initialize() {

    }

    // Event handler for the "Run one step" button
    private void handleRunOneStepButton(ActionEvent event) {
        runOneStep();
    }
}
