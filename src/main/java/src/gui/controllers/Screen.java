package src.gui.controllers;

import src.gui.controllers.*;

import java.lang.reflect.InvocationTargetException;

public enum Screen {
    CALENDAR("CalendarScreen.fxml", "CalendarScreen.css", CalendarController.class);


    private final String filename;
    private final String stylesheet;
    private final Class<? extends Controller> controllerClass;
    Screen(String filename, String stylesheet, Class<? extends Controller> controllerClass) {
        this.filename = filename;
        this.stylesheet = stylesheet;
        this.controllerClass = controllerClass;
    }

    public String getFilename() {
        return filename;
    }

    public Controller instantiateController() {
        try {
            Controller controller = controllerClass.getDeclaredConstructor().newInstance();
            controller.setFilename(filename);
            controller.setStylesheet(stylesheet);
            return controller;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
