// Login.java
import java.util.HashMap;
import java.util.Map;

public class Login {
    // Store registered users: username -> UserData
    private Map<String, UserData> registeredUsers;
    
    // Inner class to store user information
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
    }
    
    /**
     * Validates username format
     * Rules: Must contain underscore and be no more than 5 characters
     */
    public boolean checkUserName(String userName) {
        if (userName == null) return false;
        
        // Must contain underscore and be 5 characters or less
        return userName.contains("_") && userName.length() <= 5;
    }
    
    /**
     * Validates password complexity
     * Rules: At least 8 characters, capital letter, number, special character
     */
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
    
    /**
     * Validates cellphone number format
     * Rules: Must start with +27 (South African international code)
     */
    public boolean checkCellphoneNumber(String cellphone) {
        if (cellphone == null) return false;
        return cellphone.startsWith("+27");
    }
    
    /**
     * Registers a new user with validation
     * Returns appropriate success/error message
     */
    public String registerUser(String userName, String password, String cellphone, 
                              String firstName, String lastName) {
        
        // Check username format
        if (!checkUserName(userName)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        
        // Check password complexity
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        
        // Check cellphone format
        if (!checkCellphoneNumber(cellphone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        
        // If all validations pass, register the user
        registeredUsers.put(userName, new UserData(password, firstName, lastName, cellphone));
        return "Username successfully captured.";
    }
    
    /**
     * Attempts to log in a user
     * Returns true if username exists and password matches
     */
    public boolean loginUser(String userName, String password) {
        UserData user = registeredUsers.get(userName);
        return user != null && user.password.equals(password);
    }
    
    /**
     * Returns login status message
     * Success: personalized welcome message
     * Failure: generic error message
     */
    public String returnLoginStatus(String userName, String password) {
        if (loginUser(userName, password)) {
            UserData user = registeredUsers.get(userName);
            return "Welcome " + user.firstName + " " + user.lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
