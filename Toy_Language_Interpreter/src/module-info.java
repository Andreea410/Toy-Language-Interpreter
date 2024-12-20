module Toy.Language.Interpreter {
    requires javafx.controls;
    requires javafx.fxml;

    // Export the package to javafx.graphics
    exports view.gui to javafx.graphics;
    exports view.gui.selectwindow to javafx.fxml;

    // Open the package to allow reflective access to fields by FXML loader
    opens view.gui.selectwindow to javafx.fxml;
}
