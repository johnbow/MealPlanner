package src.data;

public final class SQLStatements {
    static final String DROP_MEASURES = "DROP TABLE IF EXISTS Measures";

    static final String DROP_INGREDIENTS = "DROP TABLE IF EXISTS Ingredients";

    static final String CREATE_MEASURES =
            """
            CREATE TABLE IF NOT EXISTS Measures (
                singular_name VARCHAR(256) PRIMARY KEY NOT NULL,
                plural_name VARCHAR(256) NOT NULL UNIQUE,
                abbreviation VARCHAR(32) NOT NULL,
                default_quantity REAL NOT NULL
            );
            """;

    static final String CREATE_INGREDIENTS =
            """
            CREATE TABLE IF NOT EXISTS Ingredients (
                name VARCHAR(256) PRIMARY KEY NOT NULL,
                measure_name VARCHAR(256) NOT NULL,
                measure_size REAL NOT NULL,
                calories_per_measure_size REAL NOT NULL,
                carbs REAL DEFAULT NULL,
                fat REAL DEFAULT NULL,
                protein REAL DEFAULT NULL,
                
                FOREIGN KEY (measure_name) REFERENCES Measures (singular_name)
            );
            """;

    static final String INSERT_MEASURES =
            """
            INSERT INTO Measures(singular_name, plural_name, abbreviation, default_quantity) VALUES (?,?,?,?);
            """;


    static final String SELECT_ALL_MEASURES =
            """
            SELECT * FROM Measures;
            """;
}
