package src.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class WeekdayBox extends VBox {

    private Label dayLabel;
    private Label dateLabel;

    public WeekdayBox() {
        setAlignment(Pos.CENTER);
        dayLabel = new Label();
        dayLabel.getStyleClass().add("day-label");
        dateLabel = new Label();
        dateLabel.getStyleClass().add("date-label");
        getChildren().add(dayLabel);
        getChildren().add(dateLabel);
    }

    public void setDate(LocalDate date, Locale locale) {
        dayLabel.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, locale));
        dateLabel.setText(String.valueOf(date.getDayOfMonth()));
    }
}
