package view.gui.executewindow;

import controller.Controller;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.adt.*;
import model.statements.IStmt;
import model.statements.IfStmt;
import model.states.PrgState;
import model.values.IValue;
import model.values.StringValue;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.util.*;


public class ExecuteStatementController {

    private Controller controller;

    @FXML
    private TextField numberProgramStatesTextField;

    @FXML
    private TableView<MyPair<Integer,IValue>> heapTableView;
    @FXML
    private TableColumn<MyPair<Integer,IValue>,Integer> addressColumn;
    @FXML
    private TableColumn<MyPair<Integer,IValue>,IValue> valueColumn;
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
    private ListView<Integer> identifiersListView;
    @FXML
    private Label identifiersLabel;

    @FXML
    private TableView<MyPair<String,IValue>> SymTableView;
    @FXML
    private TableColumn<MyPair<String,IValue>,String> variableNameColumn;
    @FXML
    private TableColumn<MyPair<String,IValue>,IValue> valueColumnSymTable;
    @FXML
    private Label symTableLabel;

    @FXML
    private ListView<String> executionStackListView;
    @FXML
    private Label executionStackLabel;

    @FXML
    private Button runOneStepButton;


    @FXML
    public void initialize(IStmt programStatement) {
        identifiersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addressColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getFirst()).asObject());
        valueColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getSecond())
        );

        variableNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirst()));
        valueColumnSymTable.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getSecond())
        );
        runOneStepButton.setOnMouseClicked(this::handleRunOneStep);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        populateTables();
    }

    private void populateTables() {
        populateHeapTable();
        populateSymTable();
        populateExecutionStack();
        populateOutput();
        populateFileTable();
        populateIdentifiers();
        populateNumberProgramStates();
    }


    //Functions to populate the tables

    @FXML
    private void changeProgramState(javafx.scene.input.MouseEvent mouseEvent)
    {
        populateExecutionStack();
        populateSymTable();
    }

    private PrgState getCurrentProgramState() {
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
            heapTableView.getItems().add(entry);
    }

    private void populateSymTable()
    {
        PrgState currentProgramState = getCurrentProgramState();
        var symTable = Objects.requireNonNull(currentProgramState).getSymTable();
        ArrayList<MyPair<String, IValue>> symTableContent = new ArrayList<>();
        for(String key: symTable.getContent().keySet())
            symTableContent.add(new MyPair<>(key,symTable.getContent().get(key)));
        SymTableView.getItems().clear();
        for(MyPair<String,IValue> entry: symTableContent)
            SymTableView.getItems().add(entry);
    }

    private void populateExecutionStack()
    {
        PrgState currentProgramState = getCurrentProgramState();
        IMyStack<IStmt> executionStack = Objects.requireNonNull(currentProgramState).getExeStack();
        ArrayList<IStmt> executionStackContent = new ArrayList<>();
        for(IStmt stmt: executionStack.getStack())
            executionStackContent.add(stmt);
        executionStackListView.getItems().clear();
        for(IStmt stmt: executionStackContent)
            executionStackListView.getItems().add(stmt.toString());

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

    private void populateIdentifiers() {
        List<PrgState> programStates = controller.getProgramStateList();
        List<Integer> identifiers = programStates.stream().map(PrgState::getId).toList();

        identifiers = new ArrayList<>(new HashSet<>(identifiers));

        identifiersListView.getItems().clear();
        for (Integer id : identifiers) {
            identifiersListView.getItems().add(id);
        }
    }


    private void populateNumberProgramStates()
    {
        numberProgramStatesTextField.setText(String.valueOf(controller.getProgramStateList().size()));
    }

    private void showErrorMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleRunOneStep(javafx.scene.input.MouseEvent mouseEvent)
    {
        try {
            controller.runOneStep();
            populateTables();
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }


}
