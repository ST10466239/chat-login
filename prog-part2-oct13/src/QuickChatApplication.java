import javax.swing.JOptionPane;

public class QuickChatApplication {
    private static Login loginSystem = new Login();
    private static boolean isLoggedIn = false;
    private static String currentUsername = "";
    private static String currentUserFullName = "";
    private static int maxMessages = 0;
    private static int messagesComposed = 0;

    public static void main(String[] args) {
        showWelcome();
        
        while (!isLoggedIn) {
            showLoginMenu();
        }
        
        if (isLoggedIn) {
            JOptionPane.showMessageDialog(null, "Welcome to QuickChat, " + currentUserFullName + "!");
            runApplication();
        }
    }

    private static void showWelcome() {
        JOptionPane.showMessageDialog(null, 
            "🔥 Welcome to QuickChat! 🔥\n\n" +
            "Your messages and login details are saved permanently.\n" +
            "You can login anytime to access your message history.",
            "QuickChat", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showLoginMenu() {
        String[] options = {"Login", "Register", "Exit"};
        int choice = JOptionPane.showOptionDialog(null,
                "Please choose an option:",
                "QuickChat Login",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0: // Login
                login();
                break;
            case 1: // Register
                register();
                break;
            case 2: // Exit
            case -1: // Closed dialog
                System.exit(0);
                break;
        }
    }

    private static void login() {
        String username = JOptionPane.showInputDialog("Enter username:");
        if (username == null) return; // User cancelled
        
        String password = JOptionPane.showInputDialog("Enter password:");
        if (password == null) return; // User cancelled
        
        String result = loginSystem.returnLoginStatus(username, password);
        
        if (result.startsWith("Welcome")) {
            isLoggedIn = true;
            currentUsername = username;
            currentUserFullName = loginSystem.getUserFullName(username);
            JOptionPane.showMessageDialog(null, result);
        } else {
            JOptionPane.showMessageDialog(null, 
                result + "\n\nPlease try again or register a new account.", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void register() {
        JOptionPane.showMessageDialog(null, 
            "Registration Requirements:\n" +
            "• Username: Must contain _ and be ≤5 characters\n" +
            "• Password: 8+ characters, capital, number, special character\n" +
            "• Phone: Must start with +27",
            "Registration", 
            JOptionPane.INFORMATION_MESSAGE);
        
        String username = JOptionPane.showInputDialog("Enter username (e.g., jon_1):");
        if (username == null) return;
        
        String password = JOptionPane.showInputDialog("Enter password (e.g., Pass123!):");
        if (password == null) return;
        
        String phone = JOptionPane.showInputDialog("Enter phone number (e.g., +27812345678):");
        if (phone == null) return;
        
        String firstName = JOptionPane.showInputDialog("Enter first name:");
        if (firstName == null) return;
        
        String lastName = JOptionPane.showInputDialog("Enter last name:");
        if (lastName == null) return;
        
        String result = loginSystem.registerUser(username, password, phone, firstName, lastName);
        
        if (result.equals("Username successfully captured.")) {
            JOptionPane.showMessageDialog(null, 
                "✅ Registration Successful!\n\n" +
                "Your account has been created and saved.\n" +
                "You can now login with your credentials.",
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "❌ Registration Failed:\n\n" + result,
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void runApplication() {
        setupMessageLimit();
        
        boolean running = true;
        while (running) {
            String[] options = {"Send Messages", "Show recently sent messages", "Logout"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Logged in as: " + currentUsername + "\n\nPlease choose an option:",
                    "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0: // Send Messages
                    if (messagesComposed < maxMessages) {
                        sendMessage();
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "You have reached your limit of " + maxMessages + " messages for this session.");
                    }
                    break;
                case 1: // Show recently sent messages
                    JOptionPane.showMessageDialog(null, Message.printMessages(), 
                        "Message History", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 2: // Logout
                case -1: // Closed dialog
                    running = false;
                    showFinalStatistics();
                    logout();
                    break;
            }
        }
    }

    private static void setupMessageLimit() {
        String input = JOptionPane.showInputDialog("How many messages do you wish to send this session?");
        try {
            maxMessages = Integer.parseInt(input);
            if (maxMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number.");
                setupMessageLimit();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            setupMessageLimit();
        }
    }

    private static void sendMessage() {
        // Get recipient
        String recipient = JOptionPane.showInputDialog("Enter recipient's cell number:");
        if (recipient == null) return;
        
        // Validate recipient
        String recipientValidation = Message.validateRecipientNumber(recipient);
        if (!recipientValidation.equals("Cell phone number successfully captured.")) {
            JOptionPane.showMessageDialog(null, recipientValidation);
            return;
        }

        // Get message
        String messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
        if (messageText == null) return;
        
        // Validate message length
        String messageValidation = Message.validateMessageLength(messageText);
        if (!messageValidation.equals("Message ready to send.")) {
            JOptionPane.showMessageDialog(null, messageValidation);
            return;
        }

        // Create message with sender info
        Message message = new Message(recipient, messageText);
        String result = message.sentMessage();
        
        if (result.equals("Message successfully sent.")) {
            messagesComposed++;
        }
    }

    private static void showFinalStatistics() {
        String statistics = "📊 Session Summary:\n\n" +
                           "User: " + currentUserFullName + " (" + currentUsername + ")\n" +
                           "Messages sent this session: " + messagesComposed + "\n" +
                           "Session limit: " + maxMessages + "\n" +
                           "Total messages in history: " + Message.returnTotalMessages() + "\n\n" +
                           "All your messages are saved and will be available next time you login!";
        
        JOptionPane.showMessageDialog(null, statistics, "QuickChat Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void logout() {
        int choice = JOptionPane.showConfirmDialog(null, 
            "Do you want to login with a different account?",
            "Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Reset session
            isLoggedIn = false;
            currentUsername = "";
            currentUserFullName = "";
            messagesComposed = 0;
            // Restart the app
            main(new String[]{});
        } else {
            System.exit(0);
        }
    }
}