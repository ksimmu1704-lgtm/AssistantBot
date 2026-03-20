package com.AssistantBot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIService {

    private static final String API_KEY = "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";

    public String getAIResponse(String userInput) {
        try {
        	URL url = new URL("https://api.openai.com/v1/responses");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = "{"
                    + "\"model\":\"gpt-4.1-mini\","
                    + "\"input\":\"" + userInput + "\""
                    + "}";

            OutputStream os = conn.getOutputStream();
            os.write(jsonInput.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            return ResponseParser.parse(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}