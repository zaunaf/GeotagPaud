package com.nufaza.geotagpaud;

import android.app.Application;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

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

    }

}