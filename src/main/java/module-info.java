module src {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens src to javafx.fxml;
    exports src;
    exports src.gui;
    opens src.gui to javafx.fxml;
    exports src.gui.controllers;
    opens src.gui.controllers to javafx.fxml;
    exports src.gui.components;
    opens src.gui.components to javafx.fxml;
}