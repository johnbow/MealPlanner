module src {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.harawata.appdirs;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.google.gson;
    requires org.glavo.png.javafx;
    requires org.glavo.png;

    opens src to javafx.fxml;
    exports src;
    exports src.gui;
    opens src.gui to javafx.fxml;
    exports src.gui.controllers;
    opens src.gui.controllers to javafx.fxml;
    exports src.gui.components;
    opens src.gui.components to javafx.fxml;
    exports src.data;
    opens src.data to javafx.fxml, com.google.gson;
    exports src.util;
    opens src.util to javafx.fxml;
}