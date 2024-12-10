package grupp5;

import java.sql.*;
import java.util.Scanner;

public class NoteService {



    public static void deleteNote(Scanner scanner) {
        System.out.println("Enter Note ID for deletion:");
        int noteId = scanner.nextInt();

        String sql = "DELETE FROM notes WHERE note_id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setInt(1, noteId);
                int rowsDeleted = pst.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Note deleted successfully!");
                } else {
                    System.out.println("Failed to delete note.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateNote(Scanner scanner) {
        System.out.println("Enter Note ID to update:"); // 0-?
        int noteId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter the new note:");
        String newNote = scanner.nextLine();
        System.out.println("Update favorite status? (yes/no):");
        String favoriteInput = scanner.nextLine();
        boolean isFavorite = favoriteInput.equalsIgnoreCase("yes");

        String sql = "UPDATE notes SET user_note = ?, is_favorite = ? WHERE note_id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, newNote);
                pst.setBoolean(2, isFavorite);
                pst.setInt(3, noteId);
                int rowsUpdated = pst.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Note updated successfully!");
                } else {
                    System.out.println("Failed to update note.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void viewNotes() {
        String sql = """
        SELECT n.note_id, r.name AS recipe_name, n.user_note, n.is_favorite
        FROM notes n
        JOIN recipes r ON n.recipe_id = r.recipe_id
        """;

        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    System.out.println("Note ID: " + rs.getInt("note_id"));
                    System.out.println("Recipe: " + rs.getString("recipe_name"));
                    System.out.println("Note: " + rs.getString("user_note"));
                    System.out.println("Favorite: " + (rs.getBoolean("is_favorite") ? "Yes" : "No"));
                    System.out.println("---------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addNote(Scanner scanner) {
        System.out.println("Enter Recipe ID to add a note:");
        int recipeId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter your note:");
        String userNote = scanner.nextLine();

        System.out.println("Is this a favorite? (yes/no):");
        String favoriteInput = scanner.nextLine();
        boolean isFavorite = favoriteInput.equalsIgnoreCase("yes");

        String sql = "INSERT INTO notes (recipe_id, user_note, is_favorite) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect()) {
            assert conn != null;
            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setInt(1, recipeId);
                pst.setString(2, userNote);
                pst.setBoolean(3, isFavorite);
                int rowsInserted = pst.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Note added successfully!");
                } else {
                    System.out.println("Failed to add note.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
