package com.android.studentslist;

import android.app.Application;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

public class App extends Application {

    public App(){}


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration =
                new RealmConfiguration.Builder()
                        .name("studentlist.realm")
                        .schemaVersion(1).migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                        oldVersion++;
                    }
                })
                        .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
