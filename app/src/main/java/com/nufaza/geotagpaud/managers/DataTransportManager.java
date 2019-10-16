package com.nufaza.geotagpaud.managers;

import android.content.Context;
import android.widget.Toast;

import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.model.Foto;
import com.nufaza.geotagpaud.model.Foto_Table;
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

    public static void sendData(final MainActivity context) {

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

    public static void sendDataFoto(final MainActivity context) {

        final List<Foto> fotos = SQLite.select().from(Foto.class).where(Foto_Table.status_data.eq(1)).queryList();

        if (fotos.size() > 0) {
            for (int i = 0; i < fotos.size(); i++) {
                final Foto foto = fotos.get(i);
                JSONObject jsonObject = foto.getJSONObject();

                HttpCaller hc = new HttpCaller(
                        context,
                        HttpCaller.POST,
                        "/api/fotos",
                        null,
                        jsonObject,
                        HttpCaller.RETURN_TYPE_JSON,
                        new HttpCallback() {
                            @Override
                            public void onSuccess(JSONObject responseJSO, Response response) {
                                // System.out.println(responseJSO.toString());

                                if (response.code() == 201) {

                                    foto.setStatusData(2);
                                    foto.save();
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context.getApplicationContext(), "Foto ID:" + foto.getFotoId() + " terkirim..", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context.getApplicationContext(), "Data Foto sudah terkirim..", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}