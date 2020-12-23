package com.xu.sinxiao.common.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xu.sinxiao.common.db.model.Param;

@Database(entities = {Param.class}, version = 1, exportSchema = false)
//@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ParamsDao paramsDao();
}

