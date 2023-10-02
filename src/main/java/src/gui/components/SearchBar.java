package src.gui.components;

import javafx.scene.control.TextField;

import java.util.Collection;
import java.util.Set;

public class SearchBar<T> extends TextField {

    private Collection<T> display;
    private QueryService<T> searchQuery;

    public SearchBar() {
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue))
                search(newValue);
        });
    }

    public synchronized void search(String queryString) {
        if (searchQuery == null) return;
        // First, apply filter to ingredients list and add only queried ingredients, which are not already present
        searchQuery.setSearchText(queryString);
        searchQuery.restart();
    }

    public void search() {
        search(getText());
    }

    public void setSearchQuery(QueryService<T> searchQuery) {
        this.searchQuery = searchQuery;
        this.searchQuery.setOnSucceeded(t -> setSearchResults(this.searchQuery.getResults()));
    }

    public void setDisplay(Collection<T> display) {
        this.display = display;
    }

    private void setSearchResults(Set<T> results) {
        // remove element from display if not contained in results
        // remove element from results if contained in display
        display.removeIf(t -> !results.remove(t));
        display.addAll(results);
    }

}
