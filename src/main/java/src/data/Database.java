package src.data;

import javafx.application.Platform;

import java.sql.*;
import java.util.*;

public class Database {

    private String dbURL;

    public static final String INGREDIENTS = "Ingredients";
    public static final String MEASURES = "Measures";

    private Map<String, Measure> measureMap;

    private final List<Measure> DEFAULT_MEASURES = Arrays.asList(
            new Measure("Gram", "Grams", "g", 100.0),
            new Measure("Kilogram", "Kilograms", "kg", 1.0),
            new Measure("Liter", "Liters", "l", 1.0),
            new Measure("Milliliter", "Milliliters", "ml", 100.0)
    );

    public void loadUserData(Config config) {
        measureMap = new TreeMap<>();
        dbURL = "jdbc:sqlite:" + config.getDataDirectory() + Config.DATABASE_FILE;
        createTables();
        boolean inserted = insertInitialMeasures(DEFAULT_MEASURES);
        if (!inserted)
            addAllMeasuresToMap();
    }
    private void createTables() {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                Statement stmt = con.createStatement()
        ) {
            con.setAutoCommit(false);
            if (Config.DO_CLEAN_INSTALL) {
                stmt.execute(SQLStatements.DROP_MEASURES);
                stmt.execute(SQLStatements.DROP_RECIPE_INFO);
                stmt.execute(SQLStatements.DROP_INGREDIENTS);
            }
            stmt.execute(SQLStatements.CREATE_MEASURES);
            stmt.execute(SQLStatements.CREATE_INGREDIENTS);
            stmt.execute(SQLStatements.CREATE_RECIPE_INFO);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Created tables.");
    }

    private boolean insertInitialMeasures(List<Measure> measures) {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(SQLStatements.EXISTS_ANY_MEASURE);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.INSERT_MEASURE)
        ) {
            // Check if any measure exists:
            if (!rs.next() || rs.getBoolean(1)) {
                System.out.println("There are already measures in the database.");
                return false;
            }

            con.setAutoCommit(false);
            for (Measure measure : measures) {
                pstmt.setString(1, measure.singularName());
                pstmt.setString(2, measure.pluralName());
                pstmt.setString(3, measure.abbreviation());
                pstmt.setDouble(4, measure.defaultQuantity());
                pstmt.executeUpdate();
                measureMap.put(measure.singularName(), measure);
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        for (Measure measure : measures)
            System.out.printf("Added %s to database.\n", measure.toString());
        return true;
    }

    private void addAllMeasuresToMap() {
        List<Measure> measures = getMeasuresFromTable();
        assert measures != null;
        for (Measure measure : measures)
            measureMap.put(measure.singularName(), measure);
    }

    public boolean insertIngredient(Ingredient ingredient) {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.INSERT_INGREDIENT)
        ) {
            con.setAutoCommit(false);
            pstmt.setString(1, ingredient.name());
            pstmt.setString(2, ingredient.measure().singularName());
            pstmt.setDouble(3, ingredient.measureSize());
            pstmt.setDouble(4, ingredient.caloriesPerMeasureSize());
            pstmt.setInt(5, ingredient.macroTypeInfo());
            pstmt.setDouble(6, ingredient.carbs());
            pstmt.setDouble(7, ingredient.fat());
            pstmt.setDouble(8, ingredient.protein());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.printf("Added %s to database.\n", ingredient.toString());
        return true;
    }

    public boolean insertRecipeInfo(RecipeInfo recipeInfo) {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.INSERT_RECIPE_INFO)
        ) {
            con.setAutoCommit(false);
            pstmt.setString(1, recipeInfo.name());
            pstmt.setString(2, recipeInfo.filename());
            pstmt.setInt(3, recipeInfo.servings());
            pstmt.setDouble(4, recipeInfo.total_calories());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.printf("Added %s to database.\n", recipeInfo.toString());
        return true;
    }

    public boolean removeRecipeInfo(RecipeInfo recipeInfo) {
        // TODO
        return true;
    }

    private ResultSet getAllFromTable(String table) {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt  = con.prepareStatement(SQLStatements.SELECT_ALL_FROM_TABLE);
        ) {
            pstmt.setString(1, table);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Measure> getAllMeasures() {
        if (measureMap.isEmpty())
            return getMeasuresFromTable();
        return new ArrayList<>(measureMap.values());
    }

    private List<Measure> getMeasuresFromTable() {
        List<Measure> measures = new ArrayList<>();
        try (ResultSet rs = getAllFromTable(MEASURES)) {
            if (rs == null) return null; // should not happen
            while (rs.next()) {
                measures.add(new Measure(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return measures;
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        try (ResultSet rs = getAllFromTable(INGREDIENTS)) {
            if (rs == null) return null; // should not happen
            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getString(1),
                        measureMap.get(rs.getString(2)),
                        rs.getDouble(3),
                        rs.getDouble(4),
                        rs.getInt(5),
                        rs.getDouble(6),
                        rs.getDouble(7),
                        rs.getDouble(8)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ingredients;
    }

    public synchronized void addIngredientsTo(Collection<Ingredient> ingredients, String query, int limit) {
        try(
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.SELECT_INGREDIENTS_BY_NAME);
        ) {
            pstmt.setString(1, query + "%");
            pstmt.setInt(2, limit);
            try(ResultSet rs = pstmt.executeQuery()) {
                if (!Thread.interrupted())
                    ingredients.clear();
                while (rs.next() && !Thread.interrupted()) {
                    ingredients.add(new Ingredient(
                            rs.getString(1),
                            measureMap.get(rs.getString(2)),
                            rs.getDouble(3),
                            rs.getDouble(4),
                            rs.getInt(5),
                            rs.getDouble(6),
                            rs.getDouble(7),
                            rs.getDouble(8)
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
