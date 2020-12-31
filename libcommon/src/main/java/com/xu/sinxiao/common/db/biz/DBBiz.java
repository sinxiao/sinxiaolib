package com.xu.sinxiao.common.db.biz;

import com.xu.sinxiao.common.BackgroundExecutor;
import com.xu.sinxiao.common.GsonUtils;
import com.xu.sinxiao.common.Utils;
import com.xu.sinxiao.common.callback.ResultObjectInterface;
import com.xu.sinxiao.common.db.DataBaseService;
import com.xu.sinxiao.common.db.model.BaseDBModel;
import com.xu.sinxiao.common.db.model.Param;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 针对一个实体类 的 db 保存 。此实体类需要提供继承 BaseDBModel 。其中 type 和 key 是必须定义的 ，
 * type 一般是 唯一的类名， key，是 类名的唯一标识，如 id ，将 实体 转 json 保存到 db 数据库里。
 *
 * @param <T>
 */
public class DBBiz<T extends BaseDBModel> {
    private boolean isEncrypt = false;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DBBiz() {
    }

    public void setEncrypt(boolean isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public void save(T t) {
        BackgroundExecutor.post(() -> {
            BaseDBModel baseDBModel = (BaseDBModel) t;
            Param param = new Param();
            param.setKey(baseDBModel.getKey());
            param.setType(baseDBModel.getType());
            List<Param> params = DataBaseService.getInstance().getAppDB().paramsDao().loadParamsByKeyAndType(baseDBModel.getKey(), baseDBModel.getType());
            if (params != null && params.size() > 0) {
                for (Param one : params) {
                    DataBaseService.getInstance().getAppDB().paramsDao().deleteParam(one);
                }
            }
            String value = GsonUtils.toJson(t);
            if (isEncrypt) {
                value = Utils.encryptAESLocal(value);
            }
            param.setValue(value);
            param.setCreateTime(simpleDateFormat.format(new Date()));
            DataBaseService.getInstance().getAppDB().paramsDao().insert(param);
        });
    }

    public void getBeans(String type, Class<T> clazz, ResultObjectInterface onRevObjectListener) {
        BackgroundExecutor.post(() -> {
            List<Param> params = DataBaseService.getInstance().getAppDB().paramsDao().loadParamsByType(type);
            List<T> localWallets = new ArrayList<>();
            if (params != null && !params.isEmpty()) {
                for (Param param : params) {
                    String value = param.getValue();
                    if (isEncrypt) {
                        value = Utils.dencryptAESLocal(value);
                    }

                    T t = GsonUtils.parserBean(value, clazz);
                    localWallets.add(t);
                }
            }
            if (onRevObjectListener != null) {
                onRevObjectListener.onRevData(localWallets);
            }
        });
    }

    public void getBean(String type, String key, Class<T> clazz, ResultObjectInterface resultObjectInterface) {
        BackgroundExecutor.post(() -> {
            List<Param> params = DataBaseService.getInstance().getAppDB().paramsDao().loadParamsByKeyAndType(key, type);
            List<T> beans = new ArrayList<>();
            if (params != null && !params.isEmpty()) {
                for (Param param : params) {
                    String value = param.getValue();
                    if (isEncrypt) {
                        value = Utils.dencryptAESLocal(value);
                    }

                    T t = GsonUtils.parserBean(value, clazz);
                    beans.add(t);
                }
            }
            if (beans != null && beans.size() > 0) {
                if (resultObjectInterface != null) {
                    resultObjectInterface.onRevData(beans.get(0));
                }
            } else {
                if (resultObjectInterface != null) {
                    resultObjectInterface.onRevData("");
                }
            }

        });
    }

    public void delAll(String type) {
        BackgroundExecutor.post(() -> {
            List<Param> params = DataBaseService.getInstance().getAppDB().paramsDao().loadParamsByType(type);
            if (params != null && params.size() > 0) {
                for (Param param : params) {
                    DataBaseService.getInstance().getAppDB().paramsDao().deleteParam(param);
                }
            }
        });
    }

    public void update(T t) {
        save(t);
    }

    public void delById(String id, String type) {
        List<Param> params = DataBaseService.getInstance().getAppDB().paramsDao().loadParamsByKeyAndType(id, type);
        if (params != null && params.size() > 0) {
            for (Param param : params) {
                DataBaseService.getInstance().getAppDB().paramsDao().deleteParam(param);
            }
        }
    }

    public <T> List<T> queryByTypeLikeV(String type, String v, Class<T> clazz) {
        List<Param> params = DataBaseService.getInstance().getAppDB().paramsDao().loadParamsByTypeLikeV(type, v);
        List<T> ts = new ArrayList<>();
        for (Param param : params) {
            T t = GsonUtils.parserBean(param.getValue(), clazz);
            ts.add(t);
        }
        return ts;
    }
}
