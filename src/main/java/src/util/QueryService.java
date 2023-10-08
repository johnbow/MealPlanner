package src.util;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.HashSet;
import java.util.Set;

public class QueryService<T> extends Service<Boolean> {

    private final Query<T> query;
    private final Set<T> resultSet;
    private final int resultSetCapacity;
    private String searchText;

    public QueryService(int resultSetCapacity, Query<T> query) {
        this.resultSetCapacity = resultSetCapacity;
        this.query = query;
        resultSet = new HashSet<>(resultSetCapacity);
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                resultSet.clear();
                return query.query(resultSet, searchText, resultSetCapacity);
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
