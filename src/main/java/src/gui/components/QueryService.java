package src.gui.components;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.HashSet;
import java.util.Set;

public abstract class QueryService<T> extends Service<Boolean> {

    protected Set<T> resultSet;
    private String searchText;

    public QueryService() {
        resultSet = new HashSet<>();
    }

    public QueryService(int resultSetCapacity) {
        resultSet = new HashSet<>(resultSetCapacity);
    }

    protected abstract Boolean query(String searchText) throws Exception;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return query(searchText);
            }
        };
    }

    public Set<T> getResults() {
        return resultSet;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
