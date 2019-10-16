package com.nufaza.geotagpaud.managers;

import android.content.Context;
import android.widget.Toast;

import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.model.Geotag;
import com.nufaza.geotagpaud.model.Geotag_Table;
import com.nufaza.geotagpaud.util.HttpCallback;
import com.nufaza.geotagpaud.util.HttpCaller;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Response;

public class DataTransportManager {

    public static void sendData(final MainActivity context){

        final List<Geotag> geotags = SQLite.select().from(Geotag.class).where(Geotag_Table.status_data.lessThanOrEq(1)).queryList();

        if (geotags.size() > 0) {
            for (int i = 0; i < geotags.size(); i++) {
                final Geotag geotag = geotags.get(i);
                JSONObject jsonObject = geotag.getJSONObject();

                HttpCaller hc = new HttpCaller(
                        context,
                        HttpCaller.POST,
                        "/api/geotags",
                        null,
                        jsonObject,
                        HttpCaller.RETURN_TYPE_JSON,
                        new HttpCallback() {
                            @Override
                            public void onSuccess(JSONObject responseJSO, Response response) {
                                // System.out.println(responseJSO.toString());

                                if (response.code() == 201) {

                                    geotag.setStatusData(2);
                                    geotag.save();
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context.getApplicationContext(), "Geotag ID:" + geotag.getGeotagId() + " terkirim..", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });

            }
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context.getApplicationContext(), "Data geotag sudah terkirim..", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
