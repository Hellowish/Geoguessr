package com.example.myapplication;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

public class ApiHelper {

    private static final String BASE_URL = "http://140.136.151.146/geo/coordinate.php";

    public static void fetchCoordinates(Context context, String town, String city,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {
        // 組合請求 URL
        String url = BASE_URL + "?city=" + city + "&town=" + town;

        // 建立 JSON 請求
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                listener, // 成功回應的處理器
                errorListener // 錯誤回應的處理器
        );

        // 將請求加入佇列
        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}