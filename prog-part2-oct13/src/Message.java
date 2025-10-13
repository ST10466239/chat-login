import javax.swing.JOptionPane;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;

public class Message {
    private String messageId;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String sender; // Track who sent the message
    private static int totalMessagesSent = 0;
    private static List<Message> sentMessages = new ArrayList<>();
    private static int messageCounter = 0;
    private static final String MESSAGES_FILE = "messages.json";
    
    static {
        loadMessages(); // Load messages when class is first used
    }

    public Message(String recipient, String messageText) {
        this(recipient, messageText, "");
    }
    
    public Message(String recipient, String messageText, String sender) {
        this.messageId = generateMessageID();
        this.messageNumber = ++messageCounter;
        this.recipient = recipient;
        this.messageText = messageText;
        this.sender = sender;
        this.messageHash = createMessageHash();
    }

    // Getters
    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getSender() { return sender; }
    public static int getTotalMessagesSent() { return totalMessagesSent; }

    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return this.messageId != null && this.messageId.length() == 10;
    }

    public int checkRecipientCell() {
        if (recipient == null || recipient.trim().isEmpty()) {
            return -1;
        }
        
        String cleanNumber = recipient.replaceAll("[\\s-]", "");
        
        if (cleanNumber.startsWith("+") && cleanNumber.length() <= 13) {
            String numberWithoutPlus = cleanNumber.substring(1);
            if (numberWithoutPlus.matches("\\d+") && numberWithoutPlus.length() >= 10) {
                return 0;
            }
        }
        
        if (cleanNumber.matches("\\d+") && cleanNumber.length() == 10) {
            return 0;
        }
        
        return -1;
    }

    public String createMessageHash() {
        String firstTwoId = messageId.length() >= 2 ? messageId.substring(0, 2) : messageId;
        String[] words = messageText.split("\\s+");
        String firstWord = words.length > 0 ? words[0].replaceAll("[^a-zA-Z]", "").toUpperCase() : "";
        String lastWord = words.length > 1 ? words[words.length - 1].replaceAll("[^a-zA-Z]", "").toUpperCase() : firstWord;
        
        return firstTwoId + ":" + (messageNumber - 1) + ":" + firstWord + lastWord;
    }

    public String sentMessage() {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        int choice = JOptionPane.showOptionDialog(null,
                "Message Details:\n" +
                "Message Hash: " + messageHash + "\n" +
                "Recipient: " + recipient + "\n" +
                "Message: " + messageText + "\n\n" +
                "Choose an action:",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0: // Send
                totalMessagesSent++;
                sentMessages.add(this);
                saveMessages(); // Save to file
                displayMessageDetails();
                return "Message successfully sent.";
            case 1: // Disregard
                return "Press 0 to delete message.";
            case 2: // Store
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Message disregarded.";
        }
    }

    private void displayMessageDetails() {
        String details = "Message Details:\n" +
                        "Message ID: " + messageId + "\n" +
                        "Message Hash: " + messageHash + "\n" +
                        "Recipient: " + recipient + "\n" +
                        "Message: " + messageText;
        
        JOptionPane.showMessageDialog(null, details, "Message Sent", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("All Sent Messages:\n\n");
        for (Message msg : sentMessages) {
            sb.append("Message ").append(msg.getMessageNumber()).append(":\n");
            if (!msg.getSender().isEmpty()) {
                sb.append("  From: ").append(msg.getSender()).append("\n");
            }
            sb.append("  ID: ").append(msg.getMessageId()).append("\n");
            sb.append("  Hash: ").append(msg.getMessageHash()).append("\n");
            sb.append("  To: ").append(msg.getRecipient()).append("\n");
            sb.append("  Text: ").append(msg.getMessageText()).append("\n\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public void storeMessage() {
        String json = "{\n" +
                     "  \"messageId\": \"" + messageId + "\",\n" +
                     "  \"messageNumber\": " + messageNumber + ",\n" +
                     "  \"recipient\": \"" + recipient + "\",\n" +
                     "  \"messageText\": \"" + messageText.replace("\"", "\\\"") + "\",\n" +
                     "  \"messageHash\": \"" + messageHash + "\",\n" +
                     "  \"status\": \"stored\"\n" +
                     "}";
        
        System.out.println("Storing message as JSON:");
        System.out.println(json);
    }

    public static String validateMessageLength(String message) {
        if (message.length() > 250) {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
        return "Message ready to send.";
    }

    public static String validateRecipientNumber(String recipient) {
        Message tempMsg = new Message(recipient, "test");
        int result = tempMsg.checkRecipientCell();
        if (result == 0) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    
    // Save all messages to JSON file
    private static void saveMessages() {
        try {
            StringBuilder json = new StringBuilder("{\n");
            json.append("  \"totalMessagesSent\": ").append(totalMessagesSent).append(",\n");
            json.append("  \"messages\": [\n");
            
            for (int i = 0; i < sentMessages.size(); i++) {
                Message msg = sentMessages.get(i);
                json.append("    {\n");
                json.append("      \"messageId\": \"").append(msg.messageId).append("\",\n");
                json.append("      \"messageNumber\": ").append(msg.messageNumber).append(",\n");
                json.append("      \"recipient\": \"").append(msg.recipient).append("\",\n");
                json.append("      \"messageText\": \"").append(msg.messageText.replace("\"", "\\\"")).append("\",\n");
                json.append("      \"messageHash\": \"").append(msg.messageHash).append("\",\n");
                json.append("      \"sender\": \"").append(msg.sender).append("\"\n");
                json.append("    }");
                
                if (i < sentMessages.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append("  ]\n");
            json.append("}");
            
            Files.write(Paths.get(MESSAGES_FILE), json.toString().getBytes());
        } catch (Exception e) {
            System.err.println("Error saving messages: " + e.getMessage());
        }
    }
    
    // Load messages from JSON file
    private static void loadMessages() {
        try {
            File file = new File(MESSAGES_FILE);
            if (!file.exists()) {
                return;
            }
            
            String content = new String(Files.readAllBytes(Paths.get(MESSAGES_FILE)));
            if (content.trim().isEmpty()) {
                return;
            }
            
            // Extract total messages sent
            int totalStart = content.indexOf("\"totalMessagesSent\":");
            if (totalStart != -1) {
                int numStart = content.indexOf(":", totalStart) + 1;
                int numEnd = content.indexOf(",", numStart);
                String totalStr = content.substring(numStart, numEnd).trim();
                totalMessagesSent = Integer.parseInt(totalStr);
            }
            
            // Extract messages array
            int messagesStart = content.indexOf("\"messages\":");
            if (messagesStart != -1) {
                int arrayStart = content.indexOf("[", messagesStart);
                int arrayEnd = content.lastIndexOf("]");
                
                if (arrayStart != -1 && arrayEnd != -1) {
                    String messagesContent = content.substring(arrayStart + 1, arrayEnd);
                    String[] messageBlocks = messagesContent.split("\\},\\s*\\{");
                    
                    for (String block : messageBlocks) {
                        block = block.trim();
                        if (block.isEmpty()) continue;
                        
                        // Remove leading/trailing braces if present
                        block = block.replaceAll("^\\{", "").replaceAll("\\}$", "");
                        
                        String recipient = extractJsonValue(block, "recipient");
                        String messageText = extractJsonValue(block, "messageText");
                        String sender = extractJsonValue(block, "sender");
                        String messageId = extractJsonValue(block, "messageId");
                        String messageHash = extractJsonValue(block, "messageHash");
                        int messageNumber = extractJsonInt(block, "messageNumber");
                        
                        Message msg = new Message(recipient, messageText, sender);
                        msg.messageId = messageId;
                        msg.messageHash = messageHash;
                        msg.messageNumber = messageNumber;
                        sentMessages.add(msg);
                        
                        if (messageNumber > messageCounter) {
                            messageCounter = messageNumber;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading messages: " + e.getMessage());
        }
    }
    
    private static String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return "";
            
            int valueStart = json.indexOf("\"", keyIndex + searchKey.length());
            if (valueStart == -1) return "";
            
            int valueEnd = valueStart + 1;
            while (valueEnd < json.length()) {
                if (json.charAt(valueEnd) == '"' && json.charAt(valueEnd - 1) != '\\') {
                    break;
                }
                valueEnd++;
            }
            
            return json.substring(valueStart + 1, valueEnd);
        } catch (Exception e) {
            return "";
        }
    }
    
    private static int extractJsonInt(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return 0;
            
            int valueStart = keyIndex + searchKey.length();
            int valueEnd = json.indexOf(",", valueStart);
            if (valueEnd == -1) {
                valueEnd = json.indexOf("\n", valueStart);
            }
            if (valueEnd == -1) {
                valueEnd = json.length();
            }
            
            String valueStr = json.substring(valueStart, valueEnd).trim();
            return Integer.parseInt(valueStr);
        } catch (Exception e) {
            return 0;
        }
    }
}