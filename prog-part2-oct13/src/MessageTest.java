import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    
    @Test
    public void testMessageLengthValidationSuccess() {
        String shortMessage = "This is a short message";
        String result = Message.validateMessageLength(shortMessage);
        assertEquals("Message ready to send.", result);
    }
    
    @Test
    public void testMessageLengthValidationFailure() {
        // Create a message longer than 250 characters
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            longMessage.append("a");
        }
        String result = Message.validateMessageLength(longMessage.toString());
        assertTrue(result.contains("Message exceeds 250 characters by"));
        assertTrue(result.contains("please reduce size"));
    }
    
    @Test
    public void testRecipientNumberValidationSuccess() {
        String validNumber1 = "+27718693002";
        String result1 = Message.validateRecipientNumber(validNumber1);
        assertEquals("Cell phone number successfully captured.", result1);
        
        String validNumber2 = "0831234567";
        String result2 = Message.validateRecipientNumber(validNumber2);
        assertEquals("Cell phone number successfully captured.", result2);
    }
    
    @Test
    public void testRecipientNumberValidationFailure() {
        String invalidNumber1 = "123"; // Too short
        String result1 = Message.validateRecipientNumber(invalidNumber1);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result1);
        
        String invalidNumber2 = "abc123def"; // Contains letters
        String result2 = Message.validateRecipientNumber(invalidNumber2);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result2);
    }
    
    @Test
    public void testMessageHashCreation() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        String hash = message.createMessageHash();
        
        assertNotNull(hash);
        assertTrue(hash.matches("^\\d{2}:\\d+:\\w+\\w+$"));
        assertTrue(hash.contains(":HITONIGHT"));
    }
    
    @Test
    public void testMessageIDGeneration() {
        Message message = new Message("+27718693002", "Test message");
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageId().length());
    }
    
    @Test
    public void testMessageCounterIncrement() {
        int initialCount = Message.getTotalMessagesSent();
        
        Message message1 = new Message("+27718693002", "Test message 1");
        Message message2 = new Message("08575975889", "Test message 2");
        
        // Count should still be initial until messages are sent
        assertEquals(initialCount, Message.getTotalMessagesSent());
    }
}