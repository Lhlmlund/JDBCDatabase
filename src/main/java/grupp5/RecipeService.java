package grupp5;

import java.sql.*;
import java.util.Scanner;

public class RecipeService {

    Scanner scanner = new Scanner(System.in);

    public static void markAsFavorite(int recipeId) {
        String sql = "UPDATE notes SET is_favorite = 1 WHERE recipe_id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, recipeId);
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Recipe with ID " + recipeId + " marked as favorite.");
                } else {
                    System.out.println("No recipe found with ID " + recipeId);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


        public static void showFavorites () {
            String sql = "SELECT r.recipe_id, r.name, r.description FROM recipes r " +
                    "INNER JOIN notes n ON r.recipe_id = n.recipe_id " +
                    "WHERE n.is_favorite = 1";

            try (Connection conn = DatabaseConnection.connect()) {
                assert conn != null;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    boolean hasFavorites = false;
                    while (rs.next()) {
                        hasFavorites = true;
                        System.out.println("Recipe ID: " + rs.getInt("recipe_id"));
                        System.out.println("Favorite Recipe: " + rs.getString("name"));
                        System.out.println("Description: " + rs.getString("description"));
                        System.out.println("-------------------");
                    }
                    if (!hasFavorites) {
                        System.out.println("No favorite recipes found.");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    public static void searchRecipe(String searchTerm) {
        String url = "jdbc:sqlite:E:\\JavaUtveckling2024\\SQLiteDatabase\\identifier.sqlite";
        String sql = "SELECT * FROM recipes WHERE name LIKE ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pst = conn.prepareStatement(sql)) {

            // search term with wildcard
            pst.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pst.executeQuery();

            // process the result set
            while (rs.next()) {
                System.out.println("Recipe: " + rs.getString("name"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Cook Time: " + rs.getInt("cook_time") + " minutes");
                System.out.println("Servings: " + rs.getInt("servings"));
                System.out.println("---------------------------");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteRecipe(Scanner scanner) {
        System.out.println("Enter the ID of the recipe to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM recipes WHERE recipe_id = ?";
        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, id);
                pst.executeUpdate();
                System.out.println("Recipe deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateRecipe(Scanner scanner) {
        System.out.println("Enter the ID of the recipe to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter new name: ");
        String name = scanner.nextLine();
        System.out.println("Enter new description: ");
        String description = scanner.nextLine();
        System.out.println("Enter new cook time (minutes): ");
        int cookTime = scanner.nextInt();
        System.out.println("Enter new servings: ");
        int servings = scanner.nextInt();

        String sql =
                "UPDATE recipes SET name = ?, description = ?, cook_time = ?, servings = ? WHERE recipe_id = ?";
        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, name);
                pst.setString(2, description);
                pst.setInt(3, cookTime);
                pst.setInt(4, servings);
                pst.setInt(5, id);
                pst.executeUpdate();
                System.out.println("Recipe updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void viewRecipes() {
        String sql = "SELECT * FROM recipes";
        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    System.out.println("Recipe ID: " + rs.getInt("recipe_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Description: " + rs.getString("description"));
                    System.out.println("Cook Time: " + rs.getInt("cook_time") + " minutes");
                    System.out.println("Servings: " + rs.getInt("servings"));
                    System.out.println("-----------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addRecipe(Scanner scanner) {
        System.out.println("Enter recipe name: ");
        String name = scanner.nextLine();
        System.out.println("Enter recipe description: ");
        String description = scanner.nextLine();
        System.out.println("Enter cook time (minutes): ");
        int cookTime = scanner.nextInt();
        System.out.println("Enter servings: ");
        int servings = scanner.nextInt();

        String sql = "INSERT INTO recipes (name, description, cook_time, servings) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, name);
                pst.setString(2, description);
                pst.setInt(3, cookTime);
                pst.setInt(4, servings);
                pst.executeUpdate(); // Update query
                System.out.println("Recipe added successfully!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void viewRecipesWithNotes() {
        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;

            String sql = """
        
                    SELECT r.recipe_id, r.name, r.description, r.cook_time, r.servings,
               COUNT(CASE WHEN n.is_favorite = 1 THEN 1 END) AS favorite_count,
               COUNT(CASE WHEN n.is_favorite = 0 THEN 1 END) AS non_favorite_count
        FROM recipes r
        LEFT JOIN notes n ON r.recipe_id = n.recipe_id
        GROUP BY r.recipe_id;
        """;

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    System.out.println("Recipe ID: " + rs.getInt("recipe_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Description: " + rs.getString("description"));
                    System.out.println("Cook Time: " + rs.getInt("cook_time") + " minutes");
                    System.out.println("Servings: " + rs.getInt("servings"));

                    int favoriteCount = rs.getInt("favorite_count");
                    int nonFavoriteCount = rs.getInt("non_favorite_count");

                    System.out.println("Favorite Notes Count: " + favoriteCount);
                    System.out.println("Non-Favorite Notes Count: " + nonFavoriteCount);

                    System.out.println("-------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showStatistics() {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM recipes) AS total_recipes, " +
                "(SELECT COUNT(*) FROM notes) AS total_notes, " +
                "(SELECT COUNT(*) FROM notes WHERE is_favorite = 1) AS favorite_count";

        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    System.out.println("Total Recipes: " + rs.getInt("total_recipes"));
                    System.out.println("Total Notes: " + rs.getInt("total_notes"));
                    System.out.println("Total Favorites: " + rs.getInt("favorite_count"));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


