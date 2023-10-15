package src.data;

import java.sql.*;
import java.util.*;

public class Database {

    private String dbURL;

    private final static Map<String, Measure> measureMap = new TreeMap<>();
    private final static Set<Measure> measureSet = new LinkedHashSet<>();

    private final List<Measure> DEFAULT_MEASURES = Arrays.asList(
            new Measure("Gram", "Grams", "g", 100.0, 1),
            new Measure("Milliliter", "Milliliters", "ml", 100.0, 1),
            new Measure("Kilogram", "Kilograms", "kg", 1.0, 1000),
            new Measure("Liter", "Liters", "l", 1.0, 1000)
    );

    public static Measure getMeasure(String name) {
        return measureMap.get(name);
    }

    public static Set<Measure> getMeasures() {
        return measureSet;
    }

    public void loadUserData(Config config) {
        dbURL = "jdbc:sqlite:" + config.getDataDirectory() + Config.DATABASE_FILE;
        createTables();
        boolean inserted = insertInitialMeasures(DEFAULT_MEASURES);
        if (!inserted)
            getMeasuresFromTable();
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
                pstmt.setDouble(5, measure.conversion());
                pstmt.executeUpdate();
                addMeasure(measure);
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

    public boolean insertIngredient(Ingredient ingredient) {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.INSERT_INGREDIENT)
        ) {
            con.setAutoCommit(false);
            pstmt.setString(1, ingredient.name());
            pstmt.setString(2, ingredient.measureName());
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
            System.err.println(e.getMessage());
            return false;
        }
        System.out.printf("Added %s to database.\n", recipeInfo.toString());
        return true;
    }

    public boolean removeRecipeInfo(RecipeInfo recipeInfo) {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.DELETE_RECIPE_INFO)
        ) {
            pstmt.setString(1, recipeInfo.name());
            int rowCount = pstmt.executeUpdate();
            if (rowCount > 0) {
                System.out.printf("Deleted %s from database.\n", recipeInfo.toString());
                return true;
            } else {
                System.err.printf("Couldn't delete %s from database.\n", recipeInfo.toString());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getMeasuresFromTable() {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(SQLStatements.SELECT_ALL_MEASURES)
        ) {
            if (rs == null) return; // should not happen
            while (rs.next()) {
                addMeasure(new Measure(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4),
                        rs.getDouble(5)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized boolean addIngredientsTo(Collection<Ingredient> ingredients, String query, int limit) {
        try(
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.SELECT_INGREDIENTS_BY_NAME);
        ) {
            pstmt.setString(1, query + "%");
            pstmt.setInt(2, limit);
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next() && !Thread.interrupted()) {
                    ingredients.add(new Ingredient(
                            rs.getString(1),
                            rs.getString(2),
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
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized boolean addRecipeInfosTo(Collection<RecipeInfo> recipes, String query, int limit) {
        try(
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.SELECT_RECIPE_INFOS_BY_NAME);
        ) {
            pstmt.setString(1, query + "%");
            pstmt.setInt(2, limit);
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next() && !Thread.interrupted()) {
                    recipes.add(new RecipeInfo(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getDouble(4)
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean addMeasure(Measure measure) {
        if (measureSet.contains(measure))
            return false;
        measureMap.put(measure.singularName(), measure);
        measureSet.add(measure);
        return true;
    }
}
