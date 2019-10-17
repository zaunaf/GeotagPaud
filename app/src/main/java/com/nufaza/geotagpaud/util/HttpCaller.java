package com.nufaza.geotagpaud.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;

public class HttpCaller {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okhClient;
    private Context context;
    private final HttpCallback callback;
    public final static int RETURN_TYPE_JSON = 1;
    public final static int RETURN_TYPE_FILE = 2;

    /**
     * Constructor
     * @param ctx       the context to direct toasts and dialogs
     * @param route     the route to call
     * @param params    passing parameter
     * @param cb        callback
     */
    public HttpCaller(Context ctx, String method, String route, HashMap<String, String> params, JSONObject jsonObject, final int returnType, final HttpCallback cb, String token) {
        context = ctx;
        callback = cb;

        // Check to server first, is the pengguna exists.
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(15000, TimeUnit.MILLISECONDS);
        b.writeTimeout(6000, TimeUnit.MILLISECONDS);

        okhClient = b.build();

        // Build the URL from the route and the params
        String url = ctx.getResources().getString(R.string.server_base_url) + route;
        // String url = context.getResources().getString(R.string.server_base_url) + route;

        Request request = null;

        switch (method) {

            case HttpCaller.POST:

                // If type form
                // FormBody.Builder formBodyBuilder = new FormBody.Builder();
                // for(Map.Entry<String, String> entry: params.entrySet()) {
                //     String key = entry.getKey();
                //     String val = entry.getValue();
                //     formBodyBuilder.add(key, val);
                // }
                // RequestBody requestBody = formBodyBuilder.build();

                // JSONObject userPassJson = new JSONObject(params);
                String out = jsonObject.toString();
                RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, out);

                // Build request
                request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                break;

            case HttpCaller.GET:

                Uri.Builder builder = Uri.parse(url).buildUpon();

                if (params != null) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        String key = entry.getKey();
                        String val = entry.getValue();
                        builder.appendQueryParameter(key, val);
                    }
                }

                Uri okhUri = builder.build();
                String okhUrl = okhUri.toString();

                // Build request
                if (token != null) {
                    request = new Request.Builder()
                            .addHeader("Authorization: Bearer ", token)
                            .url(okhUrl)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(okhUrl)
                            .build();
                }
                break;
        }


        okhClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                final IOException error = e;

                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Tell the user
                        MaterialDialog dialog = new MaterialDialog.Builder(context)
                                .title("Error")
                                .content("Error: " + error.getMessage())
                                .positiveText("OK")
                                .autoDismiss(true)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    switch (returnType) {
                        case RETURN_TYPE_JSON:

                            final String rspStr = response.body().string();
                            JSONObject responseJSO = new JSONObject(rspStr);

                            // if (!(boolean) responseJSO.get("success")) {
                            //
                            //     // Throw exception
                            //     throw new Exception(context.getResources().getString(R.string.http_call_failed)  + ": " +  responseJSO.get("message"));
                            // }

                            callback.onSuccess(responseJSO, response);

                            break;

                        case RETURN_TYPE_FILE:

                            String disposition = response.headers().get("Content-Disposition");
                            Long length = response.body().contentLength();
                            String filename = disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");

                            // Process download
                            InputStream in = response.body().byteStream();
                            BufferedInputStream input = new BufferedInputStream(in);

                            File outputDir = context.getCacheDir();
                            File outputPath = new File(outputDir.getPath(), filename);

                            OutputStream output = new FileOutputStream(outputPath);

                            byte[] data = new byte[1024];

                            long total = 0;
                            long progress = 0;
                            int count = 0;
                            Long i = Long.valueOf(0);

                            while ((count = input.read(data)) != -1) {
                                total += count;
                                output.write(data, 0, count);

                                // Progress
                                i++;
                                Long progressLength = i*1024;
                                Long progressPercent = (100 * progressLength) /length;
                                Integer progressPercentInt = Common.safeLongToInt(progressPercent);
                                callback.onProgress(progressLength, length, progressPercentInt);
                            }

                            output.flush();
                            output.close();
                            input.close();

                            callback.onSuccess(outputPath, response);
                            break;
                    }

                } catch (Exception e) {

                    final Exception ex = e;
                    ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } finally {

                }
            }
        });

    }

    public HttpCaller(Context ctx, String method, String route, HashMap<String, String> params, JSONObject jsonObject, final int returnType, final HttpCallback cb) {
        this(ctx, method, route, params, jsonObject, returnType, cb, null);
    }

}