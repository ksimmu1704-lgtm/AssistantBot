package com.AssistantBot;

public class ResponseParser {

    public static String parse(String json) {
        try {
            int start = json.indexOf("\"text\":\"") + 8;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } catch (Exception e) {
            return "Error parsing response";
        }
    }
}