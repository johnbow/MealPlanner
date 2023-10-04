package src.gui.components;

import javafx.application.Platform;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import src.data.Config;

public class QuantityCell<T> extends EditCell<T,Double> {
    public QuantityCell(StringConverter<Double> converter) {
        super(converter);

        // if quantityCell is edited: select all text
        editingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                Platform.runLater(() -> {
                    getTextField().requestFocus();
                    getTextField().selectAll();
                });
        });

        getTextField().setTextFormatter(new TextFormatter<>(
                new DoubleStringConverter(), 0.0, Config.DOUBLE_FILTER));
    }
}
