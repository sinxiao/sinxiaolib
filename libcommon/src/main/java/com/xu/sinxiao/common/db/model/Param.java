package com.xu.sinxiao.common.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Params")
public class Param {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String type;
    private String key_;
    /**
     * 需要进行加密
     */
    private String value;
    private String createTime;
    private String modifyTime;
    private String dataVerion;


    public Long getId() {
        return id;
    }

//    public String getKey_() {
//        return key_;
//    }
//
//    public void setKey_(String key_) {
//        this.key_ = key_;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key_;
    }

    public void setKey(String key_) {
        this.key_ = key_;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDataVerion() {
        return dataVerion;
    }

    public void setDataVerion(String dataVerion) {
        this.dataVerion = dataVerion;
    }
}
