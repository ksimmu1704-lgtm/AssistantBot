package com.AssistantBot;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

public class AssistantBot {

    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();
    static String userName = "";
    static final String CHAT_HISTORY_FILE = "conversation_log.txt";

    // Knowledge base
    static Map<String, String> knowledgeBase = new HashMap<>();
    static {
        knowledgeBase.put("java", "Java is a high-level, object-oriented, secure and platform-independent programming language and computing platform.");
        knowledgeBase.put("python", "Python is an interpreted, high-level, general-purpose programming language. It is versatile, multi-paradigm language, supporting procedural, and finctioning programming styles");
        knowledgeBase.put("c++", "C++ is a powerful, general-purpose programming language.");
        knowledgeBase.put("hello", "Hello! Is there anything i can assist you with?");
        knowledgeBase.put("hi", "Hi there! how can i help you out?");
        knowledgeBase.put("how are you", "I am your assistant bot, but I am functioning properly!");
        knowledgeBase.put("time", "Current time is " + java.time.LocalTime.now());
        knowledgeBase.put("study", "Stay consistent. Study is the foundation of a bright future.");
        knowledgeBase.put("joke", "C and C++ went to a five star bar, C was stopped by the gate guards because C got no class.");
        knowledgeBase.put("motivate", "Keep it up! Do what you can, with what you have, where you are.");
    }

    static String[] defaultReplies = {"Interesting", "Tell me more", "I see...", "Okay"};
    static String[] greetings = {"Hii! Good morning", "Hello", "Hey", "Hye"};

    public static void main(String[] args) {
        printHeader();
        loadChatHistory();

        System.out.println("Intelligent Assistant Started (type 'exit' to exit)");

        while (true) {
            System.out.print("You: ");
            String input = sc.nextLine().toLowerCase().trim();

            saveChat(input, "User");

            if (input.equals("bye")) {
                reply("Goodbye! Have a great day!");
                break;
            } else if (input.contains("my name is")) {
                userName = input.replace("my name is", "").trim();
                reply("Nice to meet you, " + userName);
            } else if (input.contains("what is my name")) {
                if (!userName.isEmpty())
                    reply("Your name is " + userName);
                else
                    reply("I don't know your name yet. Please tell me using 'my name is ...'");
            } else if (input.contains("what is your name") || input.contains("your name")) {
                reply("I am Assistant Bot AI, your offline assistant");
            } else if (input.equals("/analytics")) {
                showAnalytics();
            } else {
                String response = getResponse(input);
                reply(response);
            }
        }
        sc.close();
    }

    // Print header
    static void printHeader() {
       System.out.println("----AI Assistant----");
    }

    // Print bot reply
    static void reply(String msg) {
        System.out.println("Bot: " + msg);
        saveChat(msg, "Bot");
    }

    // Save chat to text file
    static void saveChat(String msg, String sender) {
        try (FileWriter fw = new FileWriter(CHAT_HISTORY_FILE, true)) {
            fw.write(LocalDateTime.now() + " [" + sender + "]: " + msg + "\n");
        } catch (IOException e) {
            System.out.println("Error saving chat: " + e.getMessage());
        }
    }

    // Load previous chat
    static void loadChatHistory() {
        File file = new File(CHAT_HISTORY_FILE);
        if (!file.exists()) return;

        System.out.println("-- History --");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("No previous history found.");
        }
        }
    // Generate response based on keyword matching
    static String getResponse(String input) {
        // Greeting
        for (String g : greetings)
            if (input.contains(g)) return "Hello " + (userName.isEmpty() ? "" : userName) + ". How's it going?";

        // Knowledge base match
        for (String key : knowledgeBase.keySet()) {
            if (input.contains(key)) return knowledgeBase.get(key);
        }

        // Default random reply
        return defaultReplies[rand.nextInt(defaultReplies.length)];
    }

    // Show simple analytics
    static void showAnalytics() {
        File file = new File(CHAT_HISTORY_FILE);
        if (!file.exists()) {
            System.out.println("No conversations yet.");
            return;
        }

        int totalChats = 0;
        Map<String, Integer> wordCount = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                totalChats++;
                if (line.contains("[User]:")) {
                    String msg = line.split("]:")[1].trim();
                    String[] words = msg.split("\\s+");
                    for (String w : words) wordCount.put(w, wordCount.getOrDefault(w, 0) + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("No conversations yet.");
        }

        // Top 5 words
        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordCount.entrySet());
        sortedWords.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("--- Analytics ---");
        System.out.println("Total messages: " + totalChats);
        System.out.print("Top words: ");
        for (int i = 0; i < Math.min(5, sortedWords.size()); i++) {
            System.out.print(sortedWords.get(i).getKey() + " ");
        }
    }
}