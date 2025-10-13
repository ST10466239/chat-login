import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.nio.file.*;

public class Login {
    private Map<String, UserData> registeredUsers;
    private static final String USERS_FILE = "users.json";
    
    private static class UserData {
        String password;
        String firstName;
        String lastName;
        String cellphone;
        
        UserData(String password, String firstName, String lastName, String cellphone) {
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.cellphone = cellphone;
        }
    }
    
    public Login() {
        this.registeredUsers = new HashMap<>();
        loadUsers(); // Load existing users from file
    }
    
    // Load users from JSON file
    private void loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return; // No file yet, start fresh
            }
            
            String content = new String(Files.readAllBytes(Paths.get(USERS_FILE)));
            if (content.trim().isEmpty()) {
                return;
            }
            
            // Parse simple JSON manually
            content = content.trim();
            if (content.startsWith("{") && content.endsWith("}")) {
                content = content.substring(1, content.length() - 1); // Remove outer braces
                
                // Split by user entries
                String[] userEntries = content.split("\\},\\s*\"");
                
                for (String entry : userEntries) {
                    entry = entry.trim();
                    if (entry.isEmpty()) continue;
                    
                    // Get username
                    int usernameStart = entry.indexOf("\"");
                    int usernameEnd = entry.indexOf("\"", usernameStart + 1);
                    if (usernameStart == -1 || usernameEnd == -1) continue;
                    
                    String username = entry.substring(usernameStart + 1, usernameEnd);
                    
                    // Get user data
                    String userData = entry.substring(entry.indexOf("{"));
                    String password = extractValue(userData, "password");
                    String firstName = extractValue(userData, "firstName");
                    String lastName = extractValue(userData, "lastName");
                    String cellphone = extractValue(userData, "cellphone");
                    
                    registeredUsers.put(username, new UserData(password, firstName, lastName, cellphone));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }
    
    // Save users to JSON file
    private void saveUsers() {
        try {
            StringBuilder json = new StringBuilder("{\n");
            
            boolean first = true;
            for (Map.Entry<String, UserData> entry : registeredUsers.entrySet()) {
                if (!first) json.append(",\n");
                first = false;
                
                json.append("  \"").append(entry.getKey()).append("\": {\n");
                json.append("    \"password\": \"").append(entry.getValue().password).append("\",\n");
                json.append("    \"firstName\": \"").append(entry.getValue().firstName).append("\",\n");
                json.append("    \"lastName\": \"").append(entry.getValue().lastName).append("\",\n");
                json.append("    \"cellphone\": \"").append(entry.getValue().cellphone).append("\"\n");
                json.append("  }");
            }
            
            json.append("\n}");
            
            Files.write(Paths.get(USERS_FILE), json.toString().getBytes());
        } catch (Exception e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    // Helper method to extract values from JSON string
    private String extractValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return "";
            
            int valueStart = json.indexOf("\"", keyIndex + searchKey.length());
            int valueEnd = json.indexOf("\"", valueStart + 1);
            
            if (valueStart == -1 || valueEnd == -1) return "";
            return json.substring(valueStart + 1, valueEnd);
        } catch (Exception e) {
            return "";
        }
    }
    
    public boolean checkUserName(String userName) {
        if (userName == null) return false;
        return userName.contains("_") && userName.length() <= 5;
    }
    
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return hasCapital && hasNumber && hasSpecial;
    }
    
    public boolean checkCellphoneNumber(String cellphone) {
        if (cellphone == null) return false;
        return cellphone.startsWith("+27");
    }
    
    public String registerUser(String userName, String password, String cellphone, 
                              String firstName, String lastName) {
        
        if (!checkUserName(userName)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        
        if (!checkCellphoneNumber(cellphone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        
        registeredUsers.put(userName, new UserData(password, firstName, lastName, cellphone));
        saveUsers(); // Save to file after registration
        return "Username successfully captured.";
    }
    
    public boolean loginUser(String userName, String password) {
        UserData user = registeredUsers.get(userName);
        return user != null && user.password.equals(password);
    }
    
    public String returnLoginStatus(String userName, String password) {
        if (loginUser(userName, password)) {
            UserData user = registeredUsers.get(userName);
            return "Welcome " + user.firstName + " " + user.lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
    
    // Get user's full name
    public String getUserFullName(String userName) {
        UserData user = registeredUsers.get(userName);
        if (user != null) {
            return user.firstName + " " + user.lastName;
        }
        return "";
    }
}