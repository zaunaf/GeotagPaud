package com.nufaza.geotagpaud.managers;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.R;
import com.nufaza.geotagpaud.model.Foto;
import com.nufaza.geotagpaud.model.Foto_Table;
import com.nufaza.geotagpaud.model.Geotag;
import com.nufaza.geotagpaud.model.Geotag_Table;
import com.nufaza.geotagpaud.util.HttpCallback;
import com.nufaza.geotagpaud.util.HttpCaller;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import androidx.core.content.FileProvider;
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

    public static void sendFotos(final MainActivity context){

        final List<Foto> fotos = SQLite.select().from(Foto.class).where(Foto_Table.tgl_pengiriman.isNull()).queryList();
        String url = context.getResources().getString(R.string.server_base_url) + "/uploads";

        for (int i = 0; i < fotos.size(); i++) {

            Foto foto = fotos.get(i);
            String fotoUri = foto.getFotoId().toString()+".jpg";
            String imagePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + MainActivity.EXTERNAL_IMAGE_FOLDER + "/";

            File imageFile = new File(imagePath + foto.getSekolahId() + "/"   + fotoUri);
            Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imageFile);

            try {

                UploadNotificationConfig conf = getNotificationConfig(foto.getFotoId().toString(), context);
                // conf.setTitleForAllStatuses("Geotag PAUD");
                // conf.set setCompletedMessage("Selesai mengunggah data.");
                // conf.setErrorMessage("Terjadi galat saat mengunggah data.");
                // conf.setInProgressMessage("Sedang mengunggah data");

                MultipartUploadRequest req = new MultipartUploadRequest(context, url)
                        .addFileToUpload(imageFile.toString(), "image_file")
                        .setNotificationConfig(conf)
                        .setMaxRetries(2)
                        .addParameter("sekolah_id", foto.getSekolahId().toString())
                        .addParameter("pengguna_id", foto.getPenggunaId().toString())
                        .addParameter("foto_id", foto.getFotoId().toString());

            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(), "File " + imageFile.toString() + "tidak ditemukan..", Toast.LENGTH_LONG).show();
            }

        }
    }

    private static UploadNotificationConfig getNotificationConfig(String uploadId, MainActivity context){

        UploadNotificationConfig config = new UploadNotificationConfig();

        PendingIntent clickIntent = PendingIntent.getActivity(
                context, 1, new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        config.setTitleForAllStatuses(context.getResources().getString(R.string.menu_home))
                .setRingToneEnabled(true)
                .setClickIntentForAllStatuses(clickIntent)
                .setClearOnActionForAllStatuses(true);

        config.getProgress().message = context.getResources().getString(R.string.uploading);
        config.getProgress().iconResourceID = R.drawable.ic_file_upload_black;
        config.getProgress().iconColorResourceID = Color.BLUE;

        // this is how you add an action to a specific notification status
        // config.getProgress().actions.add(new UploadNotificationAction(
        //         R.drawable.ic_stop,
        //         context.getResources().getString(R.string.cancel_upload),
        //         NotificationActions.getCancelUploadAction(this, 1, uploadId)));

        config.getCompleted().message = context.getResources().getString(R.string.upload_success);
        config.getCompleted().iconResourceID = R.drawable.ic_check;
        config.getCompleted().iconColorResourceID = Color.GREEN;

        config.getError().message = context.getResources().getString(R.string.upload_error);
        config.getError().iconResourceID = R.drawable.ic_error_outline;
        config.getError().iconColorResourceID = Color.RED;

        config.getCancelled().message = context.getResources().getString(R.string.upload_cancelled);
        config.getCancelled().iconResourceID = R.drawable.ic_error_outline;
        config.getCancelled().iconColorResourceID = Color.YELLOW;

        return config;
    }

}
