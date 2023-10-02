package src.data;

public final class SQLStatements {
    static final String DROP_MEASURES = "DROP TABLE IF EXISTS Measures";

    static final String DROP_INGREDIENTS = "DROP TABLE IF EXISTS Ingredients";

    static final String DROP_RECIPE_INFO = "DROP TABLE IF EXISTS Recipe_Info";

    static final String CREATE_MEASURES =
            """
            CREATE TABLE IF NOT EXISTS Measures (
                singular_name VARCHAR(255) PRIMARY KEY NOT NULL,
                plural_name VARCHAR(255) NOT NULL UNIQUE,
                abbreviation VARCHAR(15) NOT NULL,
                default_quantity REAL NOT NULL
            );
            """;

    static final String CREATE_INGREDIENTS =
            """
            CREATE TABLE IF NOT EXISTS Ingredients (
                name VARCHAR(255) PRIMARY KEY NOT NULL,
                measure_name VARCHAR(255) NOT NULL,
                measure_size REAL NOT NULL
                CHECK ( measure_size > 0.0 ),
                calories_per_measure_size REAL NOT NULL
                CHECK ( calories_per_measure_size >= 0.0 ),
                
                macro_type_info INT NOT NULL DEFAULT 0
                CHECK ( macro_type_info IN (0, 1, 2) ),
                carbs REAL NOT NULL DEFAULT 0.0
                CHECK ( carbs >= 0.0 ),
                fat REAL NOT NULL DEFAULT 0.0
                CHECK ( fat >= 0.0 ),
                protein REAL NOT NULL DEFAULT 0.0
                CHECK ( protein >= 0.0 ),
                
                FOREIGN KEY (measure_name) REFERENCES Measures (singular_name)
            );
            """;

    static final String CREATE_RECIPE_INFO =
            """
            CREATE TABLE IF NOT EXISTS Recipe_Info (
                name VARCHAR(255) PRIMARY KEY NOT NULL,
                filename VARCHAR(255) UNIQUE NOT NULL,
                servings INT NOT NULL DEFAULT 1
                CHECK ( servings > 0 ),
                total_calories REAL NOT NULL
                CHECK (total_calories >= 0.0)
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

    static final String INSERT_RECIPE_INFO =
            """
            INSERT INTO Recipe_Info(name, filename, servings, total_calories)
            VALUES (?,?,?,?);
            """;


    static final String SELECT_ALL_FROM_TABLE =
            """
            SELECT * FROM ?;
            """;

    static final String SELECT_INGREDIENTS_BY_NAME =
            """
            SELECT *
            FROM Ingredients
            WHERE name LIKE ?
            LIMIT ?;
            """;

    static final String EXISTS_ANY_MEASURE =
            """
            SELECT COUNT(*) FROM (SELECT 0 FROM Measures LIMIT 1);
            """;
}
