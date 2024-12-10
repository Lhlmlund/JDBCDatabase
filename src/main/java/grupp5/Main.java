package grupp5;
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("""
                \n--- Recipe Application Menu ---
                1. Add Recipe
                2. View Recipes
                3. Update Recipe
                4. Delete Recipe
                5. Add Note
                6. View Notes
                7. Update Note
                8. Delete Note
                9. View Recipes with Notes (JOIN)
                10. Show favorites
                11. Search Recipes
                12. Mark Recipe as Favorite
                13. Show Statistics
                0. Exit
                Enter your choice: """);
            choice = scanner.nextInt();
            scanner.nextLine(); // newline

            switch (choice) {
                case 1 -> RecipeService.addRecipe(scanner);
                case 2 -> RecipeService.viewRecipes();
                case 3 -> RecipeService.updateRecipe(scanner);
                case 4 -> RecipeService.deleteRecipe(scanner);
                case 5 -> NoteService.addNote(scanner);
                case 6 -> NoteService.viewNotes();
                case 7 -> NoteService.updateNote(scanner);
                case 8 -> NoteService.deleteNote(scanner);
                case 9 -> RecipeService.viewRecipesWithNotes();
                case 10 -> RecipeService.showFavorites();
                case 11 -> {
                    System.out.print("Enter recipe name, or a keyword to search: ");
                    String userinput = scanner.nextLine();
                    RecipeService.searchRecipe(userinput);}
                case 12 -> {
                    System.out.print("Enter the Recipe ID to mark as favorite: ");
                    int recipeId = scanner.nextInt();
                    RecipeService.markAsFavorite(recipeId);}
                case 13 -> RecipeService.showStatistics();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);
        scanner.close();
    }




}
