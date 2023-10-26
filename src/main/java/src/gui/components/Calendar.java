package src.gui.components;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import src.data.RecipeInfo;
import src.data.WeekTemplate;
import src.gui.GUI;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Calendar extends GridPane {

    private final WeekdayBox[] weekdayBoxes = new WeekdayBox[7];
    private final CalendarColumn[] calendarColumns = new CalendarColumn[7];

    private LocalDate firstDayOfWeek;
    private DayOfWeek weekStart;
    private Locale locale;
    private Label dateLabel;
    private GUI gui;
    private Service<WeekTemplate> weekLoader;
    private Service<Void> imageLoader;

    public Calendar() {
        setAlignment(Pos.CENTER);
        setGridLinesVisible(true);
        getRowConstraints().add(new RowConstraints());
        RowConstraints row = new RowConstraints();
        row.setMinHeight(100.0);
        row.setFillHeight(true);
        row.setVgrow(Priority.ALWAYS);
        getRowConstraints().add(row);
        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setFillWidth(true);
            col.setPercentWidth(100.0 / 7.0);
            getColumnConstraints().add(col);
            weekdayBoxes[i] = new WeekdayBox();
            add(weekdayBoxes[i], i, 0);
            calendarColumns[i] = new CalendarColumn();
            add(calendarColumns[i], i, 1);
        }
        initServices();

        // Standard values:
        setWeekStart(DayOfWeek.MONDAY);
        setLocale(Locale.ENGLISH);
    }

    public void setGui(GUI gui) {
        this.gui = gui;
        setWeekStart(gui.getConfig().getWeekStart());
        setLocale(gui.getConfig().getLanguage());
        for (CalendarColumn cell : calendarColumns)
            cell.setGui(gui);
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
        if (this.firstDayOfWeek != null && firstDayOfWeek.isEqual(this.firstDayOfWeek)) return;
        this.firstDayOfWeek = firstDayOfWeek;
        for (int i = 0; i < 7; i++) {
            weekdayBoxes[i].setDate(firstDayOfWeek.plusDays(i), locale);
        }
        if (dateLabel != null) {
            String date1 = getMonthAndYear(firstDayOfWeek);
            String date2 = getMonthAndYear(firstDayOfWeek.plusDays(6));
            if (date1.equals(date2))
                dateLabel.setText(date1);
            else
                dateLabel.setText(date1 + " - " + date2);
        }
        for (CalendarColumn cell : calendarColumns)
            cell.getChildren().clear();
        weekLoader.restart();
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
        saveDisplayedWeek();
        setWeek(firstDayOfWeek.with(TemporalAdjusters.next(weekStart)));
    }

    public void setPreviousWeek() {
        saveDisplayedWeek();
        setWeek(firstDayOfWeek.with(TemporalAdjusters.previous(weekStart)));
    }

    public WeekTemplate getWeek() {
        List<List<RecipeInfo>> days = new ArrayList<>(7);
        for (CalendarColumn cell : calendarColumns) {
            days.add(new ArrayList<>());
            for (Node node : cell.getChildren()) {
                if (node instanceof RecipeInfoBox recipeCell)
                    days.get(days.size() - 1).add(recipeCell.getRecipeInfo());
            }
        }
        return new WeekTemplate(firstDayOfWeek.toString(), days);
    }

    public void saveWeek(WeekTemplate week) {
        if (!week.isEmpty())
            gui.getConfig().getJsonLoader().write(week);
    }

    public void saveDisplayedWeek() {
        saveWeek(getWeek());
    }

    private void setWeekTemplate(WeekTemplate week) {
        if (week == null) return;
        for (int i = 0; i < 7; i++) {
            for (RecipeInfo recipeInfo : week.days().get(i))
                calendarColumns[i].addRecipeInfo(recipeInfo);
        }
        imageLoader.restart();
    }

    private void initServices() {
        weekLoader = new Service<>() {
            @Override
            protected Task<WeekTemplate> createTask() {
                return new Task<>() {
                    @Override
                    protected WeekTemplate call() {
                        return gui.getConfig().getJsonLoader().readWeek(firstDayOfWeek.toString());
                    }
                };
            }
        };
        weekLoader.setOnSucceeded(event -> setWeekTemplate(weekLoader.getValue()));
        imageLoader = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        for (CalendarColumn column : calendarColumns)
                            column.loadImages();
                        return null;
                    }
                };
            }
        };
    }

}
