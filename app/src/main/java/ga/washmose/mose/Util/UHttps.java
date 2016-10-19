package ga.washmose.mose.Util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UHttps {
    public static String IP = "https://api2.washmose.ga/v1";
    public static final MediaType JSON = MediaType.parse("application/json");
    public static final MediaType X_WWW_FORM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded");
    public static JSONObject okHttp(String urlString, RequestBody body) {
        int code;
        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(X_WWW_FORM_URLENCODED, req);

        Request request = new Request.Builder()
                .url(urlString)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject res = null;
            try {
                String responseStr = response.body().string();
                Log.d("response", responseStr);
                res = new JSONObject(responseStr);
                res.put("code", response.code());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static JSONObject okHttp(String urlString, String header, RequestBody body) {
        int code;
        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(X_WWW_FORM_URLENCODED, req);

        Request request = new Request.Builder()
                .url(urlString)
                .addHeader("api_key", header)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject res = null;
            try {
                String responseStr = response.body().string();
                res = new JSONObject(responseStr);
                res.put("code", response.code());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static JSONObject okHttp(String urlString, String header) {
        int code;
        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(X_WWW_FORM_URLENCODED, req);

        Request request = new Request.Builder()
                .url(urlString)
                .addHeader("api_key", header)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject res = null;
            try {
                String responseStr = response.body().string();
                res = new JSONObject(responseStr);
                res.put("code", response.code());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
