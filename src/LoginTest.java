// LoginTest.java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    @Test
    public void testValidUsername() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testInvalidUsername() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }
    
    @Test
    public void testValidPasswordComplexity() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Pass123!")); // Meets all rules
    }
    
    @Test
    public void testInvalidPasswordComplexity() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("pass123")); // No capital or special char
    }
    
    @Test
    public void testValidCellphoneNumber() {
        Login login = new Login();
        assertTrue(login.checkCellphoneNumber("+27612345678"));
    }
    
    @Test
    public void testInvalidCellphoneNumber() {
        Login login = new Login();
        assertFalse(login.checkCellphoneNumber("0612345678")); // Missing +27
    }
    
    @Test
    public void testRegisterUser_ValidData() {
        Login login = new Login();
        String result = login.registerUser("kyl_1", "Pass123!", "+27612345678", "Kyle", "Smith");
        assertEquals("Username successfully captured.", result);
    }
    
    @Test
    public void testRegisterUser_InvalidUsername() {
        Login login = new Login();
        String result = login.registerUser("kyle!!!!!!", "Pass123!", "+27612345678", "Kyle", "Smith");
        assertEquals("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }
    
    @Test
    public void testRegisterUser_InvalidPassword() {
        Login login = new Login();
        String result = login.registerUser("kyl_1", "pass123", "+27612345678", "Kyle", "Smith");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }
    
    @Test
    public void testRegisterUser_InvalidPhone() {
        Login login = new Login();
        String result = login.registerUser("kyl_1", "Pass123!", "0612345678", "Kyle", "Smith");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", result);
    }
    
    @Test
    public void testLogin_Successful() {
        Login login = new Login();
        login.registerUser("kyl_1", "Pass123!", "+27612345678", "Kyle", "Smith");
        assertTrue(login.loginUser("kyl_1", "Pass123!"));
    }
    
    @Test
    public void testLogin_Failed() {
        Login login = new Login();
        login.registerUser("kyl_1", "Pass123!", "+27612345678", "Kyle", "Smith");
        assertFalse(login.loginUser("kyl_1", "wrongpass"));
    }
    
    @Test
    public void testReturnLoginStatus_Success() {
        Login login = new Login();
        login.registerUser("kyl_1", "Pass123!", "+27612345678", "Kyle", "Smith");
        String result = login.returnLoginStatus("kyl_1", "Pass123!");
        assertEquals("Welcome Kyle Smith, it is great to see you again.", result);
    }
    
    @Test
    public void testReturnLoginStatus_Failure() {
        Login login = new Login();
        login.registerUser("kyl_1", "Pass123!", "+27612345678", "Kyle", "Smith");
        String result = login.returnLoginStatus("kyl_1", "wrongpass");
        assertEquals("Username or password incorrect, please try again.", result);
    }
}
