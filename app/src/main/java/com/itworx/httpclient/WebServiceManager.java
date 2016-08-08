package com.itworx.httpclient;

import android.net.SSLCertificateSocketFactory;
import android.util.Log;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by karim on 10/25/2015.
 */
public class WebServiceManager {

    public final static int GET = 1;
    public final static int POST = 2;
    private static final String TAG = WebServiceManager.class.getSimpleName();

    static HttpURLConnection conn;
    static String cookies = null;

    public static String GET(String url) {
        HttpURLConnection c = null;
        InputStream is = null;
        int status = 200;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            //Headers Params
            c.setRequestProperty("Content-Type", "application/json");
            c.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            c.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            c.setConnectTimeout(5000);
            c.setReadTimeout(5000);
            c.setRequestMethod("GET");
            //2. connect
            c.connect();
            status = c.getResponseCode();
            is = c.getInputStream();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (MalformedURLException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }

        return null;
    }

    public static String POST(String url, String json, String token) {
        StringBuilder sb;
        try {
            //constants
            Log.d(TAG, url);
            URL u = new URL(url);
            sb = new StringBuilder();
            conn = (HttpURLConnection) u.openConnection();
            if (conn instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
                httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
                conn = httpsConn;
            }

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);// to send post
            conn.setDoInput(true);
            conn.setUseCaches(true);
            conn.addRequestProperty("Cache-Control", "max-stale="
                    + (60 * 60 * 24 * 28));
            conn.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);
            conn.setAllowUserInteraction(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.addRequestProperty("Cache-Control", "max-age=0");

            if (cookies != null)
                conn.setRequestProperty("Cookie", cookies);
            conn.connect();
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(json.getBytes());
            os.flush();
            os.close();
            int status = conn.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.d(TAG, "response:" + sb.toString());
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            Log.d(TAG, e.getMessage());
        } catch (ProtocolException e) {
            Log.d(TAG, e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }



}