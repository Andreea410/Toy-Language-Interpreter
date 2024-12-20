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
import model.states.PrgState;

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
        heapTableView.getItems().clear();
    }

    private void runOneStep() {
        // Get the current program state and run one step of execution
        // After executing, you may want to update the UI elements to reflect the current state
        // Example: You would call your interpreter or program state manager here

        // For example:
        // interpreter.runOneStep();

        // Update UI components after running the step
        updateUI();
    }

    private void updateUI() {
        // Example: Update the UI elements like ListViews, TableViews, etc.

        // Update number of program states
        numberProgramStatesTextField.setText("Updated number");

        // Update heap table
        heapTableView.getItems().clear();
        heapTableView.getItems().add("Updated Heap entry");

        // Update output
        outputListView.getItems().clear();
        outputListView.getItems().add("Output text");

        // Update file table
        fileTableListView.getItems().clear();
        fileTableListView.getItems().add("File Table entry");

        // Update identifiers list
        identifiersListView.getItems().clear();
        identifiersListView.getItems().add("Identifier");

        // Update symbol table
        symTableView.getItems().clear();
        symTableView.getItems().add("Variable Name = Value");

        // Update execution stack
        executionStackListView.getItems().clear();
        executionStackListView.getItems().add("Execution Stack item");
    }


    @FXML
    public void initialize() {

        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        valueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Initialize symbol table columns
        variableNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        valueColumnSymTable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Add event listener for the "Run one step" button
        runOneStepButton.setOnAction(this::handleRunOneStepButton);
    }

    // Event handler for the "Run one step" button
    private void handleRunOneStepButton(ActionEvent event) {
        runOneStep(); // Calls the method to run one step in the program
    }
}
