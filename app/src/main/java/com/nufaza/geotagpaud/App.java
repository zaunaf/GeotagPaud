package com.nufaza.geotagpaud;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.okhttp.OkHttpStack;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize DBFlow
        FlowManager.init(FlowConfig.builder(this)
            .addDatabaseConfig(DatabaseConfig.builder(AppDatabase.class)
                .databaseName("AppDatabase")
                .build())
            .build());

        // Initialize Paths

        // Start fresco
        Fresco.initialize(this);

        // Start Gotev Multipart
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // UploadService.HTTP_STACK = new OkHttpStack();
    }

}