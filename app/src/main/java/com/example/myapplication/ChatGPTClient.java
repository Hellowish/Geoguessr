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
