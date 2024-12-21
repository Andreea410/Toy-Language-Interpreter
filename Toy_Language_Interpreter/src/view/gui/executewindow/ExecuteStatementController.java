package view.gui.executewindow;

import controller.Controller;
import javafx.application.Platform;
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
    private TableColumn<MyPair<Integer,IValue>, IValue> valueColumn;
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
        populateTables();
        identifiersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            populateExecutionStack();
            populateSymTable();
        });
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
        if (controller.getProgramStateList().isEmpty()) {
            System.out.println("No program states available.");
            return null;
        }
        int id = identifiersListView.getSelectionModel().getSelectedIndex();
        if (id == -1) {
            System.out.println("No program state selected, defaulting to index 0.");
            return controller.getProgramStateList().get(0);
        } else {
            System.out.println("Selected program state index: " + id);
            return controller.getProgramStateList().get(id);
        }
    }


    //Functions to populate the tables


    private void populateHeapTable() {
        PrgState currentProgramState = getCurrentProgramState();
        if (currentProgramState == null) {
            System.out.println("Current program state is null.");
            return;
        }

        IMyHeap heap = currentProgramState.getHeap();
        if (heap == null) {
            System.out.println("Heap is null.");
            return;
        }

        Map<Integer, IValue> heapMap = heap.getMap();
        if (heapMap.isEmpty()) {
            System.out.println("Heap is empty.");
            return;
        }

        ArrayList<MyPair<Integer, IValue>> heapContent = new ArrayList<>();
        for (Map.Entry<Integer, IValue> entry : heapMap.entrySet()) {
            heapContent.add(new MyPair<>(entry.getKey(), entry.getValue()));
        }

        System.out.println("Heap Content: " + heapContent);

        heapTableView.getItems().clear();
        heapTableView.getItems().addAll(heapContent);
        heapTableView.refresh();
    }




    private void populateSymTable() {
        int selectedProgramStateIndex = identifiersListView.getSelectionModel().getSelectedIndex();
        if (selectedProgramStateIndex == -1 || selectedProgramStateIndex >= controller.getProgramStateList().size()) {
            System.out.println("No program state selected.");
            return;
        }

        ObservableList<MyPair<String, IValue>> symTableData = FXCollections.observableArrayList(
                controller.getProgramStateList().get(selectedProgramStateIndex).getSymTable().getContent().entrySet().stream()
                        .map(entry -> new MyPair<>(entry.getKey(), entry.getValue()))
                        .toList()
        );

        variableNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirst()));
        valueColumnSymTable.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSecond()));
        SymTableView.setItems(symTableData);
        SymTableView.refresh();

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
    public void handleRunOneStep()
    {
        try {
            controller.runOneStep();
            populateTables();
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }


}
