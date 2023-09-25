package src.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {

    private String dbURL;

    private final List<Measure> DEFAULT_MEASURES = Arrays.asList(
            new Measure("Gram", "Grams", "g", 100.0),
            new Measure("Kilogram", "Kilograms", "kg", 1.0),
            new Measure("Liter", "Liters", "l", 1.0),
            new Measure("Milliliter", "Milliliters", "ml", 100.0)
    );

    public void loadUserData(Config config) {
        dbURL = "jdbc:sqlite:" + config.getDataDirectory() + Config.DATABASE_FILE;
        createTables();
        insertMeasures(DEFAULT_MEASURES);
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

    private boolean insertMeasures(List<Measure> measures) {
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
        return true;
    }

    public List<Measure> getMeasures() {
        List<Measure> measures = new ArrayList<>();
        try (
                Connection con = DriverManager.getConnection(dbURL);
                Statement stmt  = con.createStatement();
                ResultSet rs = stmt.executeQuery(SQLStatements.SELECT_ALL_MEASURES);
        ) {
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

}
