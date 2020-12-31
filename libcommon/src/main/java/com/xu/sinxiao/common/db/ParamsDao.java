package com.xu.sinxiao.common.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xu.sinxiao.common.db.model.Param;

import java.util.List;

@Dao
public interface ParamsDao {
    @Query("SELECT * FROM  Params")
    List<Param> getAllParams();

    @Query("SELECT * FROM Params WHERE type = :type")
    List<Param> loadParamsByType(String type);

    @Query("SELECT * FROM Params WHERE key_=:key and type = :type")
    List<Param> loadParamsByKeyAndType(String key, String type);

    @Query("SELECT * FROM Params WHERE type = :type and value like '%' || :v || '%' ")
    List<Param> loadParamsByTypeLikeV(String type, String v);

    @Insert
    void insert(Param param);

    @Insert
    void insertParams(Param... params);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateParams(Param... params); //更新动漫信息，当有冲突时则进行替代

    @Delete
    void deleteParam(Param param);



}
