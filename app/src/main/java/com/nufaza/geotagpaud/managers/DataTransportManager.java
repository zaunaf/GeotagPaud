package com.nufaza.geotagpaud.managers;

import android.content.Context;
import com.nufaza.geotagpaud.model.Geotag;
import com.nufaza.geotagpaud.model.Geotag_Table;
import com.nufaza.geotagpaud.util.HttpCallback;
import com.nufaza.geotagpaud.util.HttpCaller;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class DataTransportManager {

    public static void sendData(Context context){

        List<Geotag> geotags = SQLite.select().from(Geotag.class).where(Geotag_Table.status_data.eq(1)).queryList();
        // Create call to backend
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < geotags.size(); i++) {
            jsonArray.put(geotags.get(i).getJSONObject());
        }

        HttpCaller hc = new HttpCaller (
                context,
                HttpCaller.POST,
                "/api/geotags",
                null,
                jsonArray,
                HttpCaller.RETURN_TYPE_JSON,
                new HttpCallback() {
                    @Override
                    public void onSuccess(JSONObject responseJSO) {
                        System.out.println(responseJSO.toString());
                    }
                });

    }
}
