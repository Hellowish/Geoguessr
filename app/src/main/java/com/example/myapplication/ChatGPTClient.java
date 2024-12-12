package com.example.myapplication;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Callback;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ChatGPTClient {

    private Context context;

    // 構造函數接收 Context 參數
    public ChatGPTClient(Context context) {
        this.context = context;
    }

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    // 不再是靜態方法，這樣可以正常使用 context.getString()
    public void sendMessage(String userMessage, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        // 使用傳入的 context 來獲取 API Key
        String openAIKey = context.getString(R.string.openai_api_key);

        // 創建 JSON 請求體
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

        // 創建請求
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + openAIKey)
                .post(body)
                .build();

        // 异步调用
        client.newCall(request).enqueue(callback);
    }
}
