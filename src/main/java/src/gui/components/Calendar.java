package src.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class Calendar extends GridPane {

    private WeekdayBox[] weekdays = new WeekdayBox[7];
    private LocalDate firstDayOfWeek;
    private DayOfWeek weekStart;
    private Locale locale;
    private Label dateLabel;

    public Calendar() {
        setAlignment(Pos.CENTER);
        setGridLinesVisible(true);
        getRowConstraints().add(new RowConstraints());
        RowConstraints row = new RowConstraints();
        row.setFillHeight(true);
        row.setVgrow(Priority.ALWAYS);
        getRowConstraints().add(row);
        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setFillWidth(true);
            col.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(col);
            weekdays[i] = new WeekdayBox();
            add(weekdays[i], i, 0);
        }

        // Standard values:
        setWeekStart(DayOfWeek.MONDAY);
        setLocale(Locale.ENGLISH);

        setCurrentWeek();
    }

    public void setWeekStart(DayOfWeek day) {
        this.weekStart = day;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setDateLabel(Label dateLabel) {
        this.dateLabel = dateLabel;
    }

    public void setWeek(LocalDate firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
        for (int i = 0; i < 7; i++) {
            weekdays[i].setDate(firstDayOfWeek.plusDays(i), locale);
        }
        if (dateLabel != null) {
            String date1 = getMonthAndYear(firstDayOfWeek);
            String date2 = getMonthAndYear(firstDayOfWeek.plusDays(6));
            if (date1.equals(date2))
                dateLabel.setText(date1);
            else
                dateLabel.setText(date1 + " - " + date2);
        }
    }

    private String getMonthAndYear(LocalDate date) {
        String month = date.getMonth().getDisplayName(TextStyle.FULL, locale);
        String year = String.valueOf(date.getYear());
        return month + " " + year;
    }

    public void setCurrentWeek() {
        LocalDate today = LocalDate.now();
        setWeek(today.with(TemporalAdjusters.previousOrSame(weekStart)));
    }

    public void setNextWeek() {
        setWeek(firstDayOfWeek.with(TemporalAdjusters.next(weekStart)));
    }

    public void setPreviousWeek() {
        setWeek(firstDayOfWeek.with(TemporalAdjusters.previous(weekStart)));
    }

}
