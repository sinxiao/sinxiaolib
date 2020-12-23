package com.xu.sinxiao.common.db;

import android.content.Context;

import androidx.room.Room;

import java.util.Objects;

public class DataBaseService {
    private volatile static DataBaseService _db;
    private AppDatabase appDB;
    private Context context;

    public void init(Context context) {
        this.context = context;
        appDB = Room.databaseBuilder(context, AppDatabase.class, "db_infos")
                .addMigrations()
                .build();
    }

    private DataBaseService() {

    }

    public synchronized static DataBaseService getInstance() {
        if (_db == null) {
            _db = new DataBaseService();
        }
        return _db;
    }

    public AppDatabase getAppDB() {
        Objects.requireNonNull(appDB, "plz call init(context) first ");
        return appDB;
    }
}
