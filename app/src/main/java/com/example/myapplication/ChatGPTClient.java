package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Callback;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ChatGPTClient {

    private Context context;

    // 構造函數接收 Context 參數
    public ChatGPTClient(Context context) {
        this.context = context;
    }

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    // 每次執行 sendMessage 時都會從後端獲取 openAIKey
    public void sendMessage(String userMessage, Callback callback) {
        fetchApiKeyFromServer(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String openAIKey = response.getString("api");
                    sendToOpenAI(userMessage, openAIKey, callback);
                } catch (Exception e) {
                    Toast.makeText(context, "Failed to parse API key", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(context, "Error fetching API key", Toast.LENGTH_SHORT).show());
    }

    // 從後端獲取 openAIKey
    private void fetchApiKeyFromServer(Response.Listener<JSONObject> listener,
                                       Response.ErrorListener errorListener) {
        ApiHelper.fetchAPI(context, "api_key", "", listener, errorListener);
    }

    // 發送訊息到 OpenAI
    private void sendToOpenAI(String userMessage, String openAIKey, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        // 創建 JSON 請求體
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("model", "gpt-3.5-turbo");

        JsonArray messages = new JsonArray();
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("role", "user");
        messageObject.addProperty("content", userMessage);
        messages.add(messageObject);

        jsonBody.add("messages", messages);

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json"));

        // 創建請求
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + openAIKey)
                .post(body)
                .build();

        // 發送請求
        client.newCall(request).enqueue(callback);
    }
}
