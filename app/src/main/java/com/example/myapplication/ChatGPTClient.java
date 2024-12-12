package com.example.myapplication;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Callback;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChatGPTClient {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    public static void sendMessage(String userMessage, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        // 创建JSON请求体
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("model", "gpt-3.5-turbo");

        JsonArray messages = new JsonArray();
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("role", "user");
        messageObject.addProperty("content", userMessage);
        messages.add(messageObject);

        jsonBody.add("messages", messages);

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.parse("application/json")
        );

        // 创建请求
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        // 异步调用
        client.newCall(request).enqueue(callback);
    }
}
