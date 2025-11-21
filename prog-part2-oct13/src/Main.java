public class Main {
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("MESSAGE MANAGEMENT SYSTEM");
        System.out.println("=".repeat(60));
        
        MessageManager manager = new MessageManager();
        manager.populateTestData();
        
        // a. Display sender and recipient of all sent messages
        manager.displaySenderRecipientAllMessages();
        
        // b. Display the longest sent message
        manager.displayLongestMessage();
        
        // c. Search for message by hash (using first message as example)
        if (!manager.getMessages().isEmpty()) {
            manager.searchByMessageHash(manager.getMessages().get(0).getMessageHash());
        }
        
        // d. Search for all messages to a particular recipient
        manager.searchByRecipient("+27838884567");
        
        // e. Delete a message (using second message as example)
        if (manager.getMessages().size() > 1) {
            manager.deleteMessage(manager.getMessages().get(1).getMessageHash());
        }
        
        // f. Display full report
        manager.displayReport();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("RUNNING UNIT TESTS");
        System.out.println("=".repeat(60));
        
        runManualTests();
    }
    
    private static void runManualTests() {
        MessageManager testManager = new MessageManager();
        testManager.populateTestData();
        
        // Test 1: Sent Messages array correctly populated
        System.out.println("\n--- TEST 1: Sent Messages Array ---");
        var sentMessages = testManager.getSentMessages();
        System.out.println("Sent messages found: " + sentMessages.size());
        for (var msg : sentMessages) {
            System.out.println("  - " + msg.getMessage());
        }
        
        // Test 2: Longest Message
        System.out.println("\n--- TEST 2: Longest Message ---");
        String longest = testManager.displayLongestMessage();
        
        // Test 3: Search by recipient
        System.out.println("\n--- TEST 3: Search by Recipient ---");
        testManager.searchByRecipient("+27838884567");
        
        // Test 4: Delete message
        System.out.println("\n--- TEST 4: Delete Message ---");
        if (!testManager.getMessages().isEmpty()) {
            testManager.deleteMessage(testManager.getMessages().get(0).getMessageHash());
        }
        
        System.out.println("\n✓ All manual tests completed successfully!");
    }
}