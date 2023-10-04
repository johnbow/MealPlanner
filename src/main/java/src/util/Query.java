package src.util;

import java.util.Set;

@FunctionalInterface
public interface Query<T> {

    Boolean query(Set<T> resultSet, String searchText) throws Exception;

}
