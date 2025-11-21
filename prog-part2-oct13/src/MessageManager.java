import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageManager {
    private List<MessageData> messages;
    
    // Inner class to hold message data for management
    public static class MessageData {
        private String sender;
        private String recipient;
        private String message;
        private String messageHash;
        private String messageId;
        
        public MessageData(String sender, String recipient, String message, String messageHash, String messageId) {
            this.sender = sender;
            this.recipient = recipient;
            this.message = message;
            this.messageHash = messageHash;
            this.messageId = messageId;
        }
        
        // Getters
        public String getSender() { return sender; }
        public String getRecipient() { return recipient; }
        public String getMessage() { return message; }
        public String getMessageHash() { return messageHash; }
        public String getMessageId() { return messageId; }
    }
    
    public MessageManager() {
        this.messages = new ArrayList<>();
    }
    
    // Add a message to the manager
    public void addMessage(String sender, String recipient, String message, String messageHash, String messageId) {
        messages.add(new MessageData(sender, recipient, message, messageHash, messageId));
    }
    
    // Get all messages
    public List<MessageData> getMessages() {
        return messages;
    }
    
    // Get only sent messages (you can filter based on status if needed)
    public List<MessageData> getSentMessages() {
        return new ArrayList<>(messages); // For now, all messages are considered sent
    }
    
    // Populate test data
    public void populateTestData() {
        addMessage("john_", "+27838884567", "Hi Mike, can you join us for dinner tonight", 
                    "12:0:HITONIGHT", "1234567890");
        addMessage("jane_", "+27712345678", "Hello, this is a test message for the system", 
                    "34:1:HELLOSYSTEM", "2345678901");
        addMessage("bob_1", "+27838884567", "Meeting at 3pm tomorrow", 
                    "56:2:MEETINGTOMORROW", "3456789012");
        addMessage("alice", "+27823456789", "Don't forget to submit the report by Friday", 
                    "78:3:DONTFRIDAY", "4567890123");
    }
    
    // a. Display sender and recipient of all sent messages
    public void displaySenderRecipientAllMessages() {
        System.out.println("\n--- ALL SENT MESSAGES (SENDER & RECIPIENT) ---");
        System.out.println("-".repeat(60));
        
        if (messages.isEmpty()) {
            System.out.println("No messages found.");
            return;
        }
        
        for (int i = 0; i < messages.size(); i++) {
            MessageData msg = messages.get(i);
            System.out.printf("Message %d:\n", i + 1);
            System.out.printf("  Sender: %s\n", msg.getSender());
            System.out.printf("  Recipient: %s\n", msg.getRecipient());
            System.out.println();
        }
    }
    
    // b. Display the longest sent message
    public String displayLongestMessage() {
        System.out.println("\n--- LONGEST MESSAGE ---");
        System.out.println("-".repeat(60));
        
        if (messages.isEmpty()) {
            System.out.println("No messages found.");
            return "";
        }
        
        MessageData longest = messages.get(0);
        for (MessageData msg : messages) {
            if (msg.getMessage().length() > longest.getMessage().length()) {
                longest = msg;
            }
        }
        
        System.out.printf("Sender: %s\n", longest.getSender());
        System.out.printf("Recipient: %s\n", longest.getRecipient());
        System.out.printf("Message: %s\n", longest.getMessage());
        System.out.printf("Length: %d characters\n", longest.getMessage().length());
        
        return longest.getMessage();
    }
    
    // c. Search for message by hash
    public void searchByMessageHash(String hash) {
        System.out.println("\n--- SEARCH BY MESSAGE HASH ---");
        System.out.println("-".repeat(60));
        System.out.printf("Searching for hash: %s\n\n", hash);
        
        boolean found = false;
        for (MessageData msg : messages) {
            if (msg.getMessageHash().equals(hash)) {
                System.out.println("✓ Message Found!");
                System.out.printf("  Sender: %s\n", msg.getSender());
                System.out.printf("  Recipient: %s\n", msg.getRecipient());
                System.out.printf("  Message: %s\n", msg.getMessage());
                System.out.printf("  Hash: %s\n", msg.getMessageHash());
                System.out.printf("  ID: %s\n", msg.getMessageId());
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("✗ No message found with that hash.");
        }
    }
    
    // d. Search for all messages to a particular recipient
    public void searchByRecipient(String recipient) {
        System.out.println("\n--- SEARCH BY RECIPIENT ---");
        System.out.println("-".repeat(60));
        System.out.printf("Searching for recipient: %s\n\n", recipient);
        
        List<MessageData> results = messages.stream()
                .filter(msg -> msg.getRecipient().equals(recipient))
                .collect(Collectors.toList());
        
        if (results.isEmpty()) {
            System.out.println("✗ No messages found for this recipient.");
            return;
        }
        
        System.out.printf("✓ Found %d message(s):\n\n", results.size());
        for (int i = 0; i < results.size(); i++) {
            MessageData msg = results.get(i);
            System.out.printf("Message %d:\n", i + 1);
            System.out.printf("  From: %s\n", msg.getSender());
            System.out.printf("  To: %s\n", msg.getRecipient());
            System.out.printf("  Message: %s\n", msg.getMessage());
            System.out.printf("  Hash: %s\n\n", msg.getMessageHash());
        }
    }
    
    // e. Delete a message by hash
    public boolean deleteMessage(String hash) {
        System.out.println("\n--- DELETE MESSAGE ---");
        System.out.println("-".repeat(60));
        System.out.printf("Attempting to delete message with hash: %s\n\n", hash);
        
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageHash().equals(hash)) {
                MessageData deleted = messages.remove(i);
                System.out.println("✓ Message deleted successfully!");
                System.out.printf("  Sender: %s\n", deleted.getSender());
                System.out.printf("  Recipient: %s\n", deleted.getRecipient());
                System.out.printf("  Message: %s\n", deleted.getMessage());
                System.out.printf("\nRemaining messages: %d\n", messages.size());
                return true;
            }
        }
        
        System.out.println("✗ Message not found.");
        return false;
    }
    
    // f. Display full report
    public void displayReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FULL MESSAGE REPORT");
        System.out.println("=".repeat(60));
        
        System.out.printf("\nTotal Messages: %d\n", messages.size());
        
        if (messages.isEmpty()) {
            System.out.println("\nNo messages to display.");
            return;
        }
        
        // Count messages per sender
        System.out.println("\n--- MESSAGES BY SENDER ---");
        messages.stream()
                .collect(Collectors.groupingBy(MessageData::getSender, Collectors.counting()))
                .forEach((sender, count) -> 
                    System.out.printf("  %s: %d message(s)\n", sender, count));
        
        // Count messages per recipient
        System.out.println("\n--- MESSAGES BY RECIPIENT ---");
        messages.stream()
                .collect(Collectors.groupingBy(MessageData::getRecipient, Collectors.counting()))
                .forEach((recipient, count) -> 
                    System.out.printf("  %s: %d message(s)\n", recipient, count));
        
        // Display all messages
        System.out.println("\n--- ALL MESSAGES ---");
        for (int i = 0; i < messages.size(); i++) {
            MessageData msg = messages.get(i);
            System.out.printf("\nMessage %d:\n", i + 1);
            System.out.printf("  ID: %s\n", msg.getMessageId());
            System.out.printf("  Hash: %s\n", msg.getMessageHash());
            System.out.printf("  From: %s → To: %s\n", msg.getSender(), msg.getRecipient());
            System.out.printf("  Message: %s\n", msg.getMessage());
            System.out.printf("  Length: %d characters\n", msg.getMessage().length());
        }
        
        System.out.println("\n" + "=".repeat(60));
    }
}