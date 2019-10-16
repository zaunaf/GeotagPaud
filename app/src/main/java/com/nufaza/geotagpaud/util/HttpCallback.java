package com.nufaza.geotagpaud.util;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Response;

/**
 * Created by Abah on 15/12/2016.
 */
public abstract class HttpCallback {
    public void onSuccess(JSONObject responseJSO){}
    public void onSuccess(JSONObject responseJSO, Response response){}
    public void onSuccess(File responseFile ){}
    public void onSuccess(File responseFile, Response response){}
    public void onProgress(Long progress, Long total, Integer percent){}
}
