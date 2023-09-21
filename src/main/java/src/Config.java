package src;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class Config {

    private int initialHeight = 600;
    private int initialWidth = 800;
    private final UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("([0-9]*(\\.[0-9]*)?)?")) {
            return change;
        }
        return null;
    };


    public int getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(int initialHeight) {
        this.initialHeight = initialHeight;
    }

    public int getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(int initialWidth) {
        this.initialWidth = initialWidth;
    }

    public UnaryOperator<TextFormatter.Change> getDoubleFilter() {
        return doubleFilter;
    }

}
