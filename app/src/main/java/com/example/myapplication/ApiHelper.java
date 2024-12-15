package com.example.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApiHelper {

    // 原始的 URL
    private static final String BASE_URL = "http://140.136.151.146/geo/coordinate.php";

    // 新增一個插入資料的 URL (不同資料庫)
    private static final String INSERT_URL = "http://140.136.151.146/geo/insert.php";
    private static final String READ_URL = "http://140.136.151.146/geo/records.php";

    // =======================
    // 原本的 GET JSON 請求方法
    // =======================
    public static void fetchAPI(Context context, String city, String town,
                                Response.Listener<JSONObject> listener,
                                Response.ErrorListener errorListener) {
        if (Objects.equals(city, "taiwan"))
            town = "";

        String url = BASE_URL + "?city=" + city + "&town=" + town;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                listener,
                errorListener
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void fetchCoordinates(Context context, String city, String town,
                                        Response.Listener<JSONArray> listener,
                                        Response.ErrorListener errorListener) {
        if (Objects.equals(city, "taiwan"))
            town = "";

        String url = BASE_URL + "?city=" + city + "&town=" + town;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                listener,
                errorListener
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    // =======================
    // 新增的功能: 插入資料到資料庫
    // =======================
    public static void insertData(Context context, String player, int score, String city, String town,
                                  double latitude, double longitude,
                                  Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, INSERT_URL,
                listener, // 成功回應
                errorListener // 錯誤回應
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("player", player);
                params.put("score", String.valueOf(score));
                params.put("city", city);
                params.put("town", town);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                return params;
            }
        };

        // 加入請求佇列
        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // =======================
    // 新增的功能: 讀取資料庫中的 JSONArray
    // =======================
    public static void readData(Context context,
                                Response.Listener<JSONArray> listener,
                                Response.ErrorListener errorListener) {
        // 讀取資料庫的 URL
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, READ_URL, null,
                listener,
                errorListener
        );

        // 加入請求佇列
        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }
}
