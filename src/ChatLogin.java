
import java.util.Scanner;

public class ChatLoginApp {
    private static Login loginSystem = new Login();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("🔥 WELCOME TO CHAT APP 🔥");
        System.out.println("========================");
        
        while (true) {
            showMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("👋 Goodbye!");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
            System.out.println(); // Empty line for spacing
        }
    }
    
    private static void showMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Register new user");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Your choice: ");
    }
    
    private static int getChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            return -1; // Invalid choice
        } finally {
            scanner.nextLine(); // Consume newline
        }
    }
    
    private static void registerUser() {
        System.out.println("\n📝 USER REGISTRATION");
        System.out.println("-------------------");
        
        // Get username
        System.out.print("Enter username (must contain _ and be ≤5 chars): ");
        String username = scanner.nextLine();
        
        // Get password
        System.out.print("Enter password (8+ chars, capital, number, special): ");
        String password = scanner.nextLine();
        
        // Get phone number
        System.out.print("Enter phone number (must start with +27): ");
        String phone = scanner.nextLine();
        
        // Get first name
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        // Get last name
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        // Try to register
        String result = loginSystem.registerUser(username, password, phone, firstName, lastName);
        
        // Show result with appropriate icon
        if (result.equals("Username successfully captured.")) {
            System.out.println("✅ " + result);
            System.out.println("🎉 Welcome to the chat app, " + firstName + "!");
        } else {
            System.out.println("❌ " + result);
        }
    }
    
    private static void loginUser() {
        System.out.println("\n🔐 USER LOGIN");
        System.out.println("-------------");
        
        // Get credentials
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        // Try to login
        String result = loginSystem.returnLoginStatus(username, password);
        
        // Show result with appropriate icon
        if (result.startsWith("Welcome")) {
            System.out.println("✅ " + result);
            System.out.println("🚀 You're now logged into the chat!");
        } else {
            System.out.println("❌ " + result);
        }
    }
}
