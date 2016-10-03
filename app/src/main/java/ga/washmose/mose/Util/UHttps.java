package ga.washmose.mose.Util;

import android.util.Log;

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
    public static String getResponse(String urlString, JSONObject req) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);

//            trustAllHosts();

            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
//            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String s, SSLSession sslSession) {
//                    return true;
//                }
//            });

            connection = httpsURLConnection;

            //if req is null, GET
            if (req != null){
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
            }else{
                connection.setRequestMethod("GET");
            }
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoInput(true);

            if (req != null) {
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //bufferedWriter.write(getURLQuery(nameValuePairs));
                bufferedWriter.write(req.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                Log.e("UHttp", "Write : " + req.toString());
            }

            connection.connect();

            StringBuilder responseStringBuilder = new StringBuilder();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                for (; ; ) {
                    String stringLine = bufferedReader.readLine();
                    if (stringLine == null) break;
                    responseStringBuilder.append(stringLine);
                }
                bufferedReader.close();
            }

            connection.disconnect();

            Log.d("UHttps", responseStringBuilder.toString());
            return responseStringBuilder.toString();
        } catch (IOException e) {
            // 타임아웃 처리
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return null;
    }
    public static String okHttp(String urlString, RequestBody body) {

        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(X_WWW_FORM_URLENCODED, req);

        Request request = new Request.Builder()
                .url(urlString)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //SSL 인듯
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType)
                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType)
                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getURLQuery(HashMap<String, String> params){
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;

        Set<Map.Entry<String, String>> valueSet = params.entrySet();
        for (Map.Entry<String, String> entry : valueSet)
        {
            if (first)
                first = false;
            else
                stringBuilder.append("&");

            try {
                stringBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    public static String getURLObjectQuery(String key, HashMap<String, String> params){
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;

        Set<Map.Entry<String, String>> valueSet = params.entrySet();
        for (Map.Entry<String, String> entry : valueSet)
        {
            if (first)
                first = false;
            else
                stringBuilder.append("&");

            try {
                stringBuilder.append(URLEncoder.encode(key + "[" + entry.getKey() + "]", "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }
}
