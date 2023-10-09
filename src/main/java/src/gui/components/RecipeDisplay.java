package src.gui.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import src.data.RecipeInfo;
import src.gui.GUI;

import java.util.Comparator;

public class RecipeDisplay extends ScrollPane {

    private final int BORDER_WIDTH = 10;

    private final HBox contentDisplay;

    private final ObservableList<RecipeInfo> items;
    private final Comparator<RecipeInfo> itemComparator = Comparator.comparing(RecipeInfo::name);

    private GUI gui;

    public RecipeDisplay() {
        items = FXCollections.observableArrayList();
        setListener();

        contentDisplay = new HBox();
        contentDisplay.setPadding(new Insets(BORDER_WIDTH));
        contentDisplay.setSpacing(BORDER_WIDTH);
        setContent(contentDisplay);
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public ObservableList<RecipeInfo> getItems() {
        return items;
    }

    private void setListener() {
        items.addListener((ListChangeListener<RecipeInfo>) c -> {
            boolean sort = false;
            while (c.next()) {
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i)
                        updateRecipeInfo(i);
                } else if (c.wasUpdated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i)
                        updateRecipeInfo(i);
                } else if (c.wasAdded()) {
                    sort = true;
                    for (RecipeInfo item : c.getAddedSubList())
                        onRecipeInfoAdded(item);
                } else if (c.wasRemoved()) {
                    onRecipeInfoRemoved(c.getFrom(), c.getRemovedSize());
                }
            }
            if (sort)
                items.sort(itemComparator);
        });
    }

    private void onRecipeInfoAdded(RecipeInfo recipeInfo) {
        RecipeInfoBox cell = new RecipeInfoBox(gui, recipeInfo);
        cell.setPadding(new Insets(0, 5, 0, 5));
        contentDisplay.getChildren().add(cell);
    }

    private void onRecipeInfoRemoved(int place, int count) {
        for (int i = 0; i < count; i++) {
            contentDisplay.getChildren().remove(place);
        }
    }

    private void updateRecipeInfo(int position) {
        RecipeInfoBox recipeInfoBox = (RecipeInfoBox) contentDisplay.getChildren().get(position);
        recipeInfoBox.setRecipeInfo(items.get(position));
    }

}
