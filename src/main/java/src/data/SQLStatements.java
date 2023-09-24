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
                
                macro_type_info INT NOT NULL DEFAULT 0
                CHECK ( macro_type_info IN (0, 1, 2) ),
                carbs REAL NOT NULL DEFAULT 0.0,
                fat REAL NOT NULL DEFAULT 0.0,
                protein REAL NOT NULL DEFAULT 0.0,
                
                FOREIGN KEY (measure_name) REFERENCES Measures (singular_name)
            );
            """;

    static final String INSERT_MEASURE =
            """
            INSERT INTO Measures(singular_name, plural_name, abbreviation, default_quantity)
            VALUES (?,?,?,?);
            """;

    static final String INSERT_INGREDIENT =
            """
            INSERT INTO Ingredients(name, measure_name, measure_size, calories_per_measure_size, macro_type_info, carbs, fat, protein)
            VALUES (?,?,?,?,?,?,?,?);
            """;


    static final String SELECT_ALL_MEASURES =
            """
            SELECT * FROM Measures;
            """;
}
