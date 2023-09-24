package src.data;

import src.Config;

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
        dbURL = "jdbc:sqlite:" + config.getUserDataDirectory() + Config.DATABASE_FILE;
        createTables();
        insertMeasures(DEFAULT_MEASURES);
    }
    private void createTables() {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                Statement stmt = con.createStatement()
        ) {
            con.setAutoCommit(false);
            stmt.execute(SQLStatements.DROP_MEASURES);
            stmt.execute(SQLStatements.CREATE_MEASURES);
            stmt.execute(SQLStatements.DROP_INGREDIENTS);
            stmt.execute(SQLStatements.CREATE_INGREDIENTS);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Created tables.");
    }

    private void insertMeasures(List<Measure> measures) {
        try (
                Connection con = DriverManager.getConnection(dbURL);
                PreparedStatement pstmt = con.prepareStatement(SQLStatements.INSERT_MEASURES)
        ) {
            con.setAutoCommit(false);
            for (Measure measure : measures) {
                pstmt.setString(1, measure.getSingularName());
                pstmt.setString(2, measure.getPluralName());
                pstmt.setString(3, measure.getAbbreviation());
                pstmt.setDouble(4, measure.getDefaultQuantity());
                pstmt.executeUpdate();
                System.out.println("Added " + measure.toString() + " to database!");
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        rs.getString("singular_name"),
                        rs.getString("plural_name"),
                        rs.getString("abbreviation"),
                        rs.getDouble("default_quantity")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return measures;
    }

    public boolean addIngredient(Ingredient ingredient) {
        System.out.printf("Added %s to database.\n", ingredient.toString());
        // TODO: implement
        return true;    // return whether ingredient can be added
    }

}
